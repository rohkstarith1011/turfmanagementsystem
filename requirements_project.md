# AI-Based Turf Management System --- Project Requirements

## 1. Document Purpose

This document is the **project-specific requirements contract** for the
coding agent continuing the existing AI-Based Turf Management System.

The agent must use the **actual source code as the implementation source
of truth** and the supplied SRS as the functional requirements source.

The supplied project is a working Spring Boot backend. Phase 1 is
explicitly treated as complete and protected.

The remaining work is:

1.  Custom exception integration and global exception handling
    completion
2.  JWT + Spring Security
3.  Ollama integration
4.  SRS #8 AI Turf Recommendation
5.  SRS #9 AI Slot Recommendation
6.  SRS #10 AI Demand Forecasting
7.  SRS #11 AI Dynamic Pricing Simulation
8.  Backend/Postman verification
9.  Angular NON-STANDALONE frontend
10. Bootstrap + professional UI/UX + frontend validation
11. AI frontend integration
12. End-to-end verification

The SRS defines the system as an intelligent sports turf booking
platform covering search, availability, booking, payment,
cancellation/rescheduling, teams, reviews, owner/manager operations, and
AI capabilities. fileciteturn26file0L5-L22

------------------------------------------------------------------------

# 2. Source-of-Truth and Change Policy

## 2.1 Source hierarchy

Use sources in this order:

1.  Actual current project source
2.  Current SRS
3.  Existing project decisions/history
4.  Running application/database evidence
5.  General framework knowledge only where the above do not specify the
    implementation

Do not silently replace project-specific behavior with generic examples.

## 2.2 Mandatory inspection before code changes

Before modifying any existing source file, inspect the relevant:

-   `pom.xml`
-   Java version
-   Spring Boot version
-   `application.properties`
-   package structure
-   entities
-   repositories
-   DTOs
-   services
-   controllers
-   configuration
-   security
-   tests
-   database relationships
-   existing exception handling

For AI work additionally inspect:

-   facility/turf data
-   sports
-   turf-sport mappings
-   amenities
-   turf-amenity mappings
-   playing areas
-   slots
-   slot blocks
-   bookings
-   payments
-   reviews
-   user/player history
-   pricing fields
-   existing dashboard calculations

Never implement an AI feature merely from the SRS example.

------------------------------------------------------------------------

# 3. ACTUAL PROJECT BASELINE

## 3.1 Backend technology

The supplied source currently contains:

-   Java 21
-   Spring Boot 4.1.1
-   Spring Data JPA / Hibernate
-   Spring MVC
-   Spring Security
-   Bean Validation
-   Maven
-   MySQL
-   Lombok
-   Jackson
-   BCrypt password encoder

`pom.xml` currently contains:

-   `spring-boot-starter-data-jpa`
-   `spring-boot-starter-security`
-   `spring-boot-starter-webmvc`
-   `spring-boot-starter-validation`
-   MySQL connector
-   Lombok
-   Spring Boot test dependencies
-   `spring-boot-starter-webmvc-test`
-   Jackson databind

Do not add another dependency when an existing dependency can solve the
requirement.

Before adding an AI/JWT dependency, verify compatibility with **Spring
Boot 4.1.1 + Java 21**.

## 3.2 Database

Current configuration uses MySQL database:

``` text
turfmanagementsystem
```

The supplied `application.properties` currently contains the datasource
configuration and:

``` text
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Do not redesign the database.

When adding new configuration, externalize new secrets and
environment-specific values.

Do not expose JWT secrets or Ollama secrets in source control.

Do not casually rewrite existing database configuration because it
belongs to the protected Phase-1 baseline.

## 3.3 Maven wrapper

The repository contains:

``` text
mvnw
mvnw.cmd
.mvn/
```

Prefer the project Maven wrapper when available.

------------------------------------------------------------------------

# 4. ACTUAL PACKAGE ARCHITECTURE

Root package:

``` text
com.crimsonlogic.turfmanagementsystem
```

Current main packages include:

``` text
config
controller
dto.requestdtos
dto.responsedtos
entity
entity.enums
exception
repository
repository.projection
service.interfaces
service.impl
util
```

Preserve:

``` text
dto.requestdtos
dto.responsedtos
```

Do not rename these packages.

------------------------------------------------------------------------

# 5. ACTUAL DOMAIN MODEL

The current project contains these major entities:

``` text
AbstractUser
User
Role
UserRole
Player
TurfOwner
TurfManager
Admin
Coach

Facility
Sport
TurfSport
Amenity
TurfAmenity
PlayingArea

Slot
SlotBlock
Booking
BookingPlayer
Payment

CancellationPolicy
Cancellation
Review

Team
TeamPlayer

CoachingClass
CoachingClassRegistration

Notification
NotificationRequest

AuditLog
```

Current status enums include:

``` text
BookingStatus:
PENDING
CONFIRMED
CANCELLED
RESCHEDULED
COMPLETED
NO_SHOW

PaymentStatus:
INITIATED
SUCCESS
FAILED
REFUNDED

UserStatus:
ACTIVE
INACTIVE
BLOCKED
```

The SRS uses equivalent turf/slot/booking concepts and specifies the
booking statuses above. fileciteturn26file0L49-L54

------------------------------------------------------------------------

# 6. ACTUAL DOMAIN RELATIONSHIPS

The actual project uses `Facility` as the turf/facility domain object.

Do **not** introduce a second `Turf` entity merely because the SRS often
uses the word "turf".

The important existing path is:

``` text
TurfOwner
    ↓
Facility
    ↓
PlayingArea
    ↓
Slot
    ↓
Booking
    ↓
Player
```

Other important paths:

``` text
Facility
 ├── TurfSport → Sport
 ├── TurfAmenity → Amenity
 └── PlayingArea → Slot

Booking
 ├── Player
 ├── Payment
 └── Coach

Review
 ├── Player
 ├── Facility
 └── Booking

User
 └── UserRole → Role
```

Use the actual JPA mappings instead of assuming bidirectional
relationships.

------------------------------------------------------------------------

# 7. ID GENERATION

The project has an existing:

``` text
EntityIdGenerator
```

It generates IDs using:

``` text
prefix + six-digit random number
```

Existing entities generate their own IDs in `@PrePersist`.

Examples of existing prefixes include IDs for users, roles, facilities,
playing areas, slots, bookings, payments, reviews and audit logs.

**Never invent IDs during testing.**

Always use IDs returned by the running application/database.

Do not redesign the ID-generation mechanism.

------------------------------------------------------------------------

# 8. USER AND ROLE ARCHITECTURE

The existing authentication domain is:

``` text
User
Role
UserRole
```

`User` contains the password.

`UserRole` maps a user to a role and has a unique `(user_id, role_id)`
constraint.

A user may have multiple role mappings.

Existing initialized roles are:

``` text
PLAYER
OWNER
MANAGER
ADMIN
COACH
```

They are created by the existing `RoleDataInitializer`.

Do not create another role table or another user model.

------------------------------------------------------------------------

# 9. FACILITY ARCHITECTURE

The existing `Facility` contains:

-   facility ID
-   name
-   owner
-   manager
-   location
-   address
-   locality
-   city
-   state
-   capacity
-   opening time
-   closing time
-   base price
-   rules
-   rating
-   availability
-   status

The SRS requires turf management information including owner, location,
sports, capacity, facilities/amenities, operating hours, price, rating,
availability and status. fileciteturn26file0L23-L27

Reuse the existing Facility architecture.

------------------------------------------------------------------------

# 10. SLOT ARCHITECTURE

The actual project uses:

``` text
Facility
 → PlayingArea
    → Slot
    → SlotBlock
```

`Slot` contains:

-   slot ID
-   playing area
-   slot date
-   start time
-   end time
-   status

The SRS requires configurable operating hours, slot duration,
peak/off-peak hours, maintenance/holiday blocking, slot capacity/price
and real-time availability. fileciteturn26file0L35-L48

Existing slot/booking logic is protected.

AI must consume the existing availability logic rather than replacing
it.

------------------------------------------------------------------------

# 11. BOOKING ARCHITECTURE

`Booking` currently contains:

-   booking ID
-   player
-   slot
-   booking date
-   start time
-   end time
-   number of players
-   price
-   discount
-   tax
-   total amount
-   payment
-   need-coach flag
-   booking status
-   created timestamp
-   updated timestamp
-   coach

Existing `BookingRepository` already contains booking lookup/counting
and pessimistic-locking functionality for booking updates.

The SRS requires duplicate-slot prevention and slot locking during
checkout/payment. fileciteturn26file0L49-L54

Do not bypass the normal booking service for AI slot selection.

------------------------------------------------------------------------

# 12. PAYMENT ARCHITECTURE

`Payment` is one-to-one with `Booking`.

Existing payment statuses:

``` text
INITIATED
SUCCESS
FAILED
REFUNDED
```

The SRS requires these statuses and requires failed payments to release
the reserved slot. fileciteturn26file0L135-L139

AI features must not alter the existing payment workflow.

------------------------------------------------------------------------

# 13. REVIEW/RATING ARCHITECTURE

`Review` is linked to:

``` text
Player
Facility
Booking
```

It contains:

-   rating
-   comment
-   timestamps

Current review logic requires the booking to be completed and updates
the Facility rating from the average review rating.

The SRS requires customer rating/review functionality and review
analysis in the broader AI scope. fileciteturn26file0L122-L127

Only the existing rating data is relevant to the current AI
turf-recommendation scope.

Do not implement SRS sentiment analysis.

------------------------------------------------------------------------

# 14. AUDIT LOG ARCHITECTURE

The current project already has:

``` text
AuditLog
AuditLogRepository
AuditLogResponseDTO
```

`AuditLog` contains:

-   audit log ID
-   action
-   entity type
-   entity ID
-   old value
-   new value
-   performedBy
-   timestamp

The current source does **not** show an existing service/controller
integration using `AuditLog`; do not assume audit logging is already
wired into every administrative action.

The SRS explicitly requires administrative actions to be recorded in
audit logs. fileciteturn26file0L254-L268

Pricing approval must preserve auditability.

If pricing history needs additional persistence, inspect this existing
model first and add only the smallest required extension.

------------------------------------------------------------------------

# 15. CURRENT EXCEPTION STATE

The current source already contains:

``` text
exception/
├── BadRequestException.java
├── ResourceNotFoundException.java
└── GlobalExceptionHandler.java
```

The current handler maps:

``` text
ResourceNotFoundException → 404
BadRequestException → 400
IllegalArgumentException → 400 fallback
```

The response shape currently contains:

``` json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "..."
}
```

## 15.1 Existing verified exception migration

`TurfOwnerDashboardServiceImpl` currently uses:

``` java
new ResourceNotFoundException("Turf owner not found: " + ownerId)
```

Do not revert this.

## 15.2 Known incorrect classification

`AmenityServiceImpl` currently uses `ResourceNotFoundException` for:

``` text
Amenity with this name already exists
```

That is a duplicate/business validation and should use
`BadRequestException`.

Change only the exception classification and preserve the existing
business rule/message.

## 15.3 Remaining exception migration

Inspect existing `IllegalArgumentException` usage individually.

Use:

``` text
ResourceNotFoundException
```

for genuinely missing resources.

Use:

``` text
BadRequestException
```

for invalid input/business operations.

Do not mass-replace all `IllegalArgumentException`.

Keep the generic fallback until migration is safely complete.

------------------------------------------------------------------------

# 16. VALIDATION ERROR HANDLING

The current DTOs already use Bean Validation and most controllers use:

``` java
@Valid @RequestBody
```

Existing request DTO validation includes, where applicable:

-   `@NotBlank`
-   `@NotNull`
-   `@Size`
-   `@Email`
-   `@Pattern`
-   `@Min`
-   `@Max`
-   `@DecimalMin`
-   `@DecimalMax`

The agent must add centralized validation-error handling compatible with
the actual Spring Boot version.

Do not remove DTO validation.

Do not duplicate all validation logic inside the exception handler.

The backend remains authoritative.

------------------------------------------------------------------------

# 17. PHASE 1 --- PROTECTED FUNCTIONALITY

The following is considered existing Phase-1 functionality:

-   backend architecture
-   entities/models
-   DTOs
-   repositories
-   services
-   controllers
-   REST APIs
-   database integration
-   user management
-   role mapping
-   player management
-   turf-owner management
-   turf-manager management
-   admin management
-   coach management
-   sports
-   amenities
-   facilities
-   turf-sport mappings
-   turf-amenity mappings
-   playing areas
-   slots
-   slot blocks
-   bookings
-   booking players
-   payments
-   cancellation policies
-   cancellation
-   rescheduling
-   teams
-   team players
-   coaching classes
-   coaching class registrations
-   notifications
-   notification requests
-   reviews/ratings
-   Turf Owner Dashboard
-   Admin Dashboard
-   business validations
-   Postman/API testing already performed during project development

Do not rebuild these.

The SRS requires Turf Owner Dashboard metrics including bookings,
occupancy, revenue, cancellation/no-show rate, sports, peak hours and
ratings; the existing backend dashboard work is therefore part of the
protected baseline. fileciteturn26file0L145-L158

The SRS also requires Admin Dashboard metrics including users, turfs,
active turfs, bookings, revenue, cancellations, locations, sports, peak
hours, occupancy and owner/facility performance.
fileciteturn26file0L159-L171

------------------------------------------------------------------------

# 18. CURRENT SECURITY STATE

Spring Security is already present.

`SecurityConfig` exists.

A BCrypt `PasswordEncoder` bean already exists.

`UserServiceImpl` already hashes passwords using the injected
`PasswordEncoder`.

The current `SecurityConfig` disables CSRF and permits the existing
`/api/...` development endpoints listed in the configuration, with
unmatched requests requiring authentication.

JWT authentication is **not yet implemented**.

Do not create a second authentication system.

------------------------------------------------------------------------

# 19. JWT + SPRING SECURITY REQUIREMENTS

Implement JWT using the existing:

``` text
User
Role
UserRole
UserStatus
PasswordEncoder
SecurityConfig
```

Required capabilities:

-   login endpoint
-   credential verification
-   JWT generation
-   JWT validation
-   JWT request filter
-   authentication context
-   role authorities
-   public endpoint configuration
-   protected endpoints
-   401 handling
-   403 handling
-   RBAC
-   facility ownership authorization
-   appropriate user-status handling

Before coding, inspect:

``` text
UserServiceImpl
UserController
UserRoleServiceImpl
RoleRepository
UserRoleRepository
RoleDataInitializer
SecurityConfig
```

Do not invent role names.

## 19.1 Public/protected routes

Determine actual access from:

-   SRS role responsibilities
-   current controllers
-   actual domain relationships
-   current role data

Do not blindly make every endpoint public.

## 19.2 Ownership

The SRS requires that turf owners can manage only authorized facilities.
fileciteturn26file0L254-L268

Implement ownership checks against the actual:

``` text
Facility.owner
```

Do not authorize ownership using a request-body owner ID alone.

------------------------------------------------------------------------

# 20. AI SCOPE --- ABSOLUTE LOCK

Implement **ONLY** these four AI requirements:

``` text
SRS #8 — AI Turf Recommendation
SRS #9 — AI Slot Recommendation
SRS #10 — AI Demand Forecasting
SRS #11 — AI Dynamic Pricing Simulation
```

The SRS defines these requirements on pages 2--3.
fileciteturn26file0L55-L81

Do not implement:

-   cancellation/no-show prediction
-   player/team matching
-   team match recommendation
-   revenue forecasting
-   maintenance prediction
-   review sentiment analysis
-   chatbot
-   AI booking assistant
-   generic AI assistant
-   AI customer-support chatbot
-   turf-owner chatbot
-   unrelated recommendation systems
-   unrelated prediction systems

The SRS contains these other capabilities, but they are explicitly
outside the current implementation scope.
fileciteturn26file0L82-L134

------------------------------------------------------------------------

# 21. AI ARCHITECTURE

Preferred logical flow:

``` text
Angular
   ↓
Spring Boot Controller
   ↓
AI Service
   ↓
Actual application data
   ↓
Deterministic filtering/rules
   ↓
Controlled AI prompt/input
   ↓
Ollama
   ↓
Structured AI output
   ↓
Response validation
   ↓
Deterministic re-check
   ↓
Application DTO
   ↓
Angular
```

The SRS recommends deterministic business rules for booking,
authorization and pricing limits, while using AI/ML for recommendations
and predictions. fileciteturn26file0L269-L273

AI is not the final authority.

------------------------------------------------------------------------

# 22. AI DATA SAFETY

Never send the entire database to Ollama.

Only send relevant sanitized information.

Never send:

-   passwords
-   JWT secrets
-   database credentials
-   unnecessary personal information
-   unrelated entities
-   internal configuration
-   unrestricted database dumps

The backend must retrieve the data and decide which candidates are valid
before AI processing.

------------------------------------------------------------------------

# 23. AI #8 --- TURF RECOMMENDATION

The SRS requires analysis of:

-   location
-   distance
-   sport
-   budget
-   preferred time
-   previous bookings
-   ratings
-   amenities
-   turf quality
-   group size

It requires:

-   ranked turf recommendations
-   reasons
-   exclusion of unavailable turfs
-   exclusion of unavailable slots

fileciteturn26file0L55-L62

Integrate with actual project data:

``` text
Facility
TurfSport
Sport
TurfAmenity
Amenity
PlayingArea
Slot
SlotBlock
Booking
Review
pricing
location
```

where relevant.

### Required sequence

``` text
Validate request
↓
Authorize user
↓
Retrieve actual candidate facilities
↓
Filter inactive/unavailable facilities
↓
Filter unsupported sport
↓
Filter unavailable slots
↓
Apply budget/other deterministic constraints
↓
Build controlled AI input
↓
Call Ollama
↓
Validate AI result
↓
Verify returned IDs belong to candidate set
↓
Return response DTO
```

Never allow an unavailable facility/slot to survive because AI selected
it.

Never hardcode a match score.

------------------------------------------------------------------------

# 24. AI #9 --- SLOT RECOMMENDATION

The SRS requires recommendation using:

-   user preference
-   historical booking behavior
-   price
-   demand
-   availability
-   weather where integrated
-   travel distance

It must explain the recommendation and allow select/reject.
fileciteturn26file0L63-L67

Integrate with the existing:

``` text
Slot
Booking
PlayingArea
Facility
pricing
availability
```

logic.

Selection must lead back into the normal booking workflow.

AI must never confirm a booking automatically.

Weather is optional and must not cause unnecessary external
infrastructure.

------------------------------------------------------------------------

# 25. AI #10 --- DEMAND FORECASTING

The SRS requires demand prediction based on:

-   day
-   time
-   sport
-   season
-   holidays
-   historical bookings
-   local events
-   optional weather

It requires:

-   Low/Medium/High classification
-   expected future occupancy
-   owner decision support for staffing and pricing

fileciteturn26file0L68-L73

Use actual historical booking data wherever available.

Do not fabricate historical data.

If there is insufficient history, return a safe
limitation/low-confidence response rather than pretending the forecast
is highly accurate.

------------------------------------------------------------------------

# 26. AI #11 --- DYNAMIC PRICING SIMULATION

The SRS requires recommended pricing based on:

-   base price
-   demand
-   remaining capacity
-   peak hours
-   holidays
-   historical occupancy

Recommendations must remain within owner-configured minimum and maximum
limits. fileciteturn26file0L74-L77

The SRS explicitly requires owner approval before applying AI pricing
and pricing history for audit/analysis. fileciteturn26file0L79-L81

## 26.1 Absolute rule

AI pricing is a recommendation/simulation.

It must **never automatically update the actual price**.

Required flow:

``` text
Generate AI recommendation
↓
Owner reviews
↓
Owner explicitly approves OR rejects
↓
If rejected → do not update price
↓
If approved:
    verify owner authorization
    verify facility ownership
    verify min/max limits again
    apply actual price
    record pricing history/audit
```

The backend must re-check limits during approval even if the
recommendation was already validated.

------------------------------------------------------------------------

# 27. PRICING LIMIT ENFORCEMENT

Deterministically enforce:

``` text
minimumPrice <= recommendedPrice <= maximumPrice
```

Never trust an LLM to obey this.

If the AI returns an out-of-range value:

``` text
reject the AI result
```

Do not silently clamp it and pretend the AI returned the valid value
unless the implementation explicitly documents that deterministic
post-processing policy.

Owner approval is still mandatory.

------------------------------------------------------------------------

# 28. OLLAMA INTEGRATION

No Ollama/Spring-AI/LLM integration is present in the supplied current
source.

No AI classes or AI configuration were found in the current backend.

Before selecting an integration approach, inspect:

-   Java version
-   Spring Boot version
-   current Maven dependencies
-   current architecture

Use the smallest compatible approach.

A direct HTTP integration with Ollama is acceptable if it is safer/more
compatible than adding a large framework.

Do not blindly add Spring AI or another framework without checking
compatibility with Spring Boot 4.1.1.

## 28.1 Externalized configuration

At minimum externalize:

``` text
Ollama base URL
Ollama model
connection/read timeout values
```

Use application configuration/environment variables.

Do not hardcode these throughout services.

Do not commit secrets.

## 28.2 Operational documentation

Document:

1.  Install Ollama
2.  Start Ollama
3.  Pull the selected model
4.  Configure Spring Boot
5.  Start backend
6.  Test each AI endpoint

## 28.3 Failure handling

Handle:

-   Ollama unavailable
-   connection failure
-   timeout
-   empty response
-   malformed response
-   invalid JSON
-   missing required AI fields
-   invalid IDs
-   out-of-range values

Do not return fake AI results when Ollama fails.

Return a controlled application error.

------------------------------------------------------------------------

# 29. STRUCTURED AI RESPONSES

Prefer structured JSON rather than free-form text.

## Turf recommendation

Minimum conceptual fields:

``` text
facilityId
facilityName
matchScore
reasons
distance
price
availability
```

Add only fields actually required by the UI/API.

## Slot recommendation

Conceptually:

``` text
facility
slotId
score
reasons
price
demand
availability
```

## Demand forecast

Conceptually:

``` text
date
time/slot
sport
demandLevel
expectedOccupancy
explanation
```

Demand level must be:

``` text
LOW
MEDIUM
HIGH
```

## Pricing recommendation

Conceptually:

``` text
basePrice
demandLevel
recommendedPrice
minimumPrice
maximumPrice
factors
approvalStatus
```

Do not create these DTOs until the actual existing DTO style and naming
conventions have been inspected.

------------------------------------------------------------------------

# 30. AI OUTPUT VALIDATION

The backend must validate LLM output.

At minimum verify:

-   valid JSON
-   required fields
-   correct types
-   valid numeric ranges
-   valid facility/slot IDs
-   returned IDs exist
-   returned IDs belong to candidate set
-   availability remains valid
-   pricing remains within configured limits

If the AI invents an ID:

``` text
reject result
```

If the AI recommends an unavailable slot:

``` text
reject result
```

If the AI violates owner pricing limits:

``` text
reject result
```

The backend remains authoritative.

------------------------------------------------------------------------

# 31. AI PERSISTENCE

Do not create every AI table mentioned by the SRS.

The SRS lists suggested tables such as:

``` text
recommendations
demand_predictions
price_predictions
cancellation_predictions
player_matches
revenue_predictions
maintenance_predictions
chat_history
audit_logs
```

These are suggestions, not a requirement to implement all of them.
fileciteturn26file0L172-L205

For the current four-feature scope:

-   add persistence only where it materially supports the feature;
-   reuse existing domain tables;
-   reuse `AuditLog` where appropriate;
-   do not create unused AI tables.

------------------------------------------------------------------------

# 32. REST API STRATEGY

The SRS suggests AI endpoints including:

``` text
GET /api/ai/recommendations/{userId}
GET /api/ai/slot-recommendations/{userId}
GET /api/ai/demand/{turfId}
GET /api/ai/pricing/{turfId}
```

fileciteturn26file0L206-L236

These are the natural conceptual API targets, but the agent must inspect
the current project before choosing exact mappings, request DTOs, query
parameters and controller structure.

Do not create duplicate endpoints if equivalent functionality already
exists.

For request-dependent AI operations, prefer a request DTO when the
required inputs cannot safely be represented by a path variable alone.

------------------------------------------------------------------------

# 33. BACKEND TESTING --- POSTMAN

Current development verification is direct API/Postman testing.

For each change:

``` text
Compile
↓
Start backend
↓
Postman request
↓
Inspect HTTP status/body
↓
Verify database state where applicable
↓
Document result
```

Do not claim manual testing without evidence.

## 33.1 Exception tests

Test:

-   resource not found → 404
-   bad request/business validation → 400
-   validation failure → centralized validation response
-   unexpected application failure → controlled response

## 33.2 Security tests

Test:

-   valid login
-   invalid password
-   missing token
-   invalid token
-   expired token where implemented
-   valid role
-   wrong role
-   401
-   403
-   owner accessing own facility
-   owner accessing another owner's facility
-   admin-only endpoint

## 33.3 AI tests

Test:

-   turf recommendation
-   slot recommendation
-   demand forecast
-   pricing simulation
-   invalid AI input
-   no matching candidates
-   unavailable resources
-   Ollama unavailable
-   malformed AI response
-   invalid AI ID
-   out-of-range pricing
-   owner rejection
-   owner approval
-   actual price update after approval
-   audit/history after approval

Always use actual IDs returned by successful API calls.

------------------------------------------------------------------------

# 34. TESTING EVIDENCE RULE

Every test record must distinguish:

``` text
ACTUALLY TESTED
```

from:

``` text
REQUIRES MANUAL POSTMAN VERIFICATION
```

Do not mark an endpoint as passed merely because it compiles.

Do not claim Ollama works without an actual Ollama call.

Do not claim JWT works without actual authenticated requests.

Do not claim pricing approval works without verifying the database
change and audit/history effect.

------------------------------------------------------------------------

# 35. ANGULAR FRONTEND --- CURRENT STATE

The supplied project source contains **no Angular application**.

No:

``` text
package.json
angular.json
src/app
```

frontend structure was found in the supplied project ZIP.

Therefore Angular must be introduced as the frontend implementation
phase rather than assumed to already exist.

------------------------------------------------------------------------

# 36. ANGULAR ARCHITECTURE --- HARD REQUIREMENT

Use:

> **Angular NON-STANDALONE / module-based architecture**

This is mandatory.

Use:

``` text
AppModule
Feature Modules
Routing Modules
Components
Services
Guards
HTTP Interceptors
```

Do not use standalone components.

Do not convert the application to standalone architecture.

------------------------------------------------------------------------

# 37. BOOTSTRAP --- HARD REQUIREMENT

Use Bootstrap for the Angular UI.

Before adding it:

1.  inspect generated Angular project dependencies;
2.  choose a version compatible with the selected Angular version;
3.  add only necessary UI dependencies.

Bootstrap should provide the base structure for:

-   layout
-   navigation
-   forms
-   buttons
-   cards
-   tables
-   alerts
-   modals
-   responsive behavior
-   dashboards

Custom CSS is allowed for application identity.

Do not add multiple competing UI frameworks without strong
justification.

------------------------------------------------------------------------

# 38. PROFESSIONAL SPORTS/TURF UI

The UI must look like a real:

> Sports / Turf Management + Booking + AI application

It must not look like:

-   basic student CRUD
-   default Angular demo
-   unstyled Bootstrap
-   unrelated generic admin template

The visual language should communicate:

-   sports
-   turf
-   energy
-   professionalism
-   modern technology
-   AI-powered management

Keep the UI clean and usable rather than flashy.

------------------------------------------------------------------------

# 39. APPLICATION-WIDE DESIGN SYSTEM

Before building pages, establish one design system.

Define:

``` text
primary
secondary
accent
background
surface/card
text
muted text
success
warning
danger
border/neutral
```

Choose a professional sports/turf palette.

After selection:

> The same palette must be used throughout the application.

Do not invent unrelated colors for each module.

Apply the system consistently to:

-   navbar
-   sidebar
-   buttons
-   forms
-   cards
-   tables
-   dashboard metrics
-   alerts
-   AI screens
-   status indicators
-   modals

Avoid excessive gradients and excessive colors.

------------------------------------------------------------------------

# 40. FRONTEND VALIDATION --- HARD REQUIREMENT

Every relevant Angular form must implement proper field validation.

Validation must be derived from:

1.  SRS
2.  existing backend DTO validation
3.  actual backend business constraints where appropriate

Validate, where applicable:

-   required
-   min/max length
-   email
-   phone
-   numeric values
-   positive values
-   price
-   date
-   time
-   date/time relationships
-   selections
-   group size
-   budget
-   booking fields
-   turf/facility fields
-   user fields
-   login fields
-   AI request fields
-   minimum/maximum pricing

Examples from the existing backend:

``` text
User:
name 2–100
email max 150 + email format
password 8–100
phone pattern

Facility:
name 2–150
capacity >= 1
base price > 0
opening time / closing time
rules max 5000

Booking:
numberOfPlayers >= 1
price/discount/tax/totalAmount >= 0

Review:
rating 1–5
```

Do not assume frontend rules are complete until all current DTOs have
been inspected.

Use user-friendly messages.

Do not rely only on HTML attributes.

Use Angular form validation appropriate to the module-based
architecture.

Frontend validation improves UX; backend validation remains
authoritative.

------------------------------------------------------------------------

# 41. FRONTEND ERROR HANDLING

Handle at least:

``` text
400
401
403
404
409
500
AI failure
Ollama unavailable
network failure
```

Display useful user-facing messages.

Never display raw stack traces.

Use the centralized backend error response format where applicable.

------------------------------------------------------------------------

# 42. FRONTEND AUTHENTICATION

Implement actual backend authentication:

``` text
Login
 ↓
Spring Boot
 ↓
JWT
 ↓
Angular
 ↓
JWT handling/storage according to chosen security approach
 ↓
HTTP interceptor
 ↓
Protected API
```

Implement:

-   AuthService
-   HTTP interceptor
-   route guards
-   logout
-   session handling
-   role-aware navigation
-   role-aware access

Do not create fake authentication.

------------------------------------------------------------------------

# 43. FRONTEND FEATURE AREAS

Create appropriate modules/components for:

-   authentication
-   navigation
-   user-facing booking workflows
-   facility/turf management
-   slot management
-   dashboards
-   owner/admin views
-   relevant forms
-   AI features

Build only what the backend actually supports.

Do not create UI for nonexistent APIs.

------------------------------------------------------------------------

# 44. AI FRONTEND --- TURF RECOMMENDATION

Provide user inputs for relevant recommendation preferences.

Display:

-   ranked facilities/turfs
-   match score
-   reasons
-   distance
-   price
-   availability
-   relevant facility information

Never display an unavailable candidate as recommended.

------------------------------------------------------------------------

# 45. AI FRONTEND --- SLOT RECOMMENDATION

Display:

-   recommended slot
-   time
-   price
-   demand where available
-   availability
-   reason

Allow:

``` text
Select
Reject
```

Selection must continue through the existing booking workflow.

Do not directly confirm a booking from AI UI.

------------------------------------------------------------------------

# 46. AI FRONTEND --- DEMAND FORECAST

Owner-facing screen should show:

-   future date
-   slot/time
-   sport
-   Low/Medium/High demand
-   expected occupancy
-   explanation
-   useful interpretation for staffing/pricing

Use real API results.

Do not fabricate charts/data.

------------------------------------------------------------------------

# 47. AI FRONTEND --- DYNAMIC PRICING

Owner-facing UI must show:

-   current/base price
-   demand
-   recommended price
-   minimum price
-   maximum price
-   recommendation factors
-   approval status

Provide explicit:

``` text
Approve
Reject
```

Never automatically apply the price from the UI.

After approval, refresh actual pricing from the backend.

------------------------------------------------------------------------

# 48. END-TO-END AI ARCHITECTURE

Final AI integration must be genuine.

## Turf recommendation

``` text
Angular
 ↓
Spring Boot
 ↓
actual Facility/Sport/Amenity/Slot/Booking/Review data
 ↓
deterministic filtering
 ↓
Ollama
 ↓
validated recommendation
 ↓
Spring Boot DTO
 ↓
Angular
```

## Slot recommendation

``` text
Angular
 ↓
Spring Boot
 ↓
actual availability/history/pricing
 ↓
Ollama
 ↓
validated result
 ↓
Angular
```

## Demand forecast

``` text
Owner Angular UI
 ↓
Spring Boot
 ↓
historical booking data
 ↓
Ollama
 ↓
forecast
 ↓
Angular
```

## Pricing

``` text
Owner Angular UI
 ↓
Spring Boot
 ↓
actual demand/occupancy/pricing data
 ↓
Ollama
 ↓
recommendation
 ↓
owner approval
 ↓
deterministic authorization + limits
 ↓
actual price update
 ↓
audit/history
 ↓
Angular refresh
```

------------------------------------------------------------------------

# 49. SECURITY + AI

AI endpoints are protected APIs.

Authorization must be based on the actual authenticated user and role.

Do not accept:

``` text
ownerId
userId
facilityId
```

from the client and blindly trust it as proof of authorization.

For owner operations:

``` text
authenticated owner
→ verify facility ownership
→ perform operation
```

For player-specific operations:

``` text
authenticated player
→ verify requested player belongs to authenticated user
```

For admin operations:

``` text
authenticated ADMIN role
```

Do not weaken security to make Postman/Angular testing easier.

------------------------------------------------------------------------

# 50. SRS BUSINESS RULES THAT AI MUST NOT BREAK

The SRS states:

1.  A slot cannot be booked twice.
2.  A slot must be locked during checkout/payment.
3.  Failed payments must release the slot.
4.  A turf cannot be booked outside operating hours.
5.  Maintenance-blocked slots cannot be booked.
6.  Booking capacity must not exceed configured limits.
7.  Cancellation/refund rules are configurable.
8.  Dynamic pricing must remain within owner-configured limits.
9.  AI recommendations must consider actual slot availability.
10. AI booking suggestions must not confirm a booking without customer
    confirmation.
11. Player matching must protect privacy.
12. AI predictions are decision-support information.
13. Turf owners can manage only authorized facilities.
14. Administrative actions must be recorded in audit logs.

fileciteturn26file0L254-L268

For this project's current AI scope, rules 1--10, 12--14 are especially
important.

------------------------------------------------------------------------

# 51. GIT/CHECKPOINTS

The supplied repository contains Git history.

Use Git checkpoints when possible.

Recommended milestones:

``` text
Existing Phase-1 baseline
↓
Exceptions complete
↓ commit

Security complete
↓ commit

Ollama integration complete
↓ commit

AI #8 complete
↓ commit

AI #9 complete
↓ commit

AI #10 complete
↓ commit

AI #11 complete
↓ commit

Backend verification complete
↓ commit

Angular foundation complete
↓ commit

Frontend validation/UI complete
↓ commit

AI frontend complete
↓ commit

E2E complete
↓ final commit
```

Never intentionally destroy the known working baseline.

------------------------------------------------------------------------

# 52. INTERRUPTION / SESSION RESILIENCE

The project may be developed across multiple agent sessions.

The agent must maintain a concise progress record, preferably:

``` text
.agent-progress.md
```

The record must state:

``` text
Current phase
Completed phases
Completed tasks
Pending tasks
Files modified
Tests performed
Known issues
Next action
```

Do not rely on previous conversational memory.

The repository itself must contain enough progress information for
another agent session to resume safely.

------------------------------------------------------------------------

# 53. AGENT WORKING LOOP

Every logical change must follow:

``` text
INSPECT
 ↓
PLAN
 ↓
IMPLEMENT
 ↓
COMPILE
 ↓
TEST
 ↓
FIX
 ↓
RE-TEST
 ↓
DOCUMENT
 ↓
CHECKPOINT
 ↓
NEXT
```

Do not jump randomly between phases.

Do not implement multiple unrelated architectural changes in one
uncontrolled patch.

------------------------------------------------------------------------

# 54. DEFINITION OF DONE --- BACKEND

Backend is complete only when:

-   Phase-1 APIs still work
-   existing business validations still work
-   custom exception classification is appropriate
-   global exception handling is consistent
-   validation errors are centralized
-   JWT login works
-   password verification works
-   JWT validation works
-   protected endpoints work
-   401 works
-   403 works
-   RBAC works
-   owner/facility authorization works
-   Ollama integration works
-   Ollama configuration is externalized
-   AI #8 works
-   AI #9 works
-   AI #10 works
-   AI #11 works
-   AI uses actual application data
-   AI does not violate deterministic rules
-   unavailable candidates are rejected
-   pricing stays within limits
-   owner approval is mandatory
-   pricing history/auditability is maintained
-   AI/Ollama failures are handled
-   backend APIs are verified

------------------------------------------------------------------------

# 55. DEFINITION OF DONE --- FRONTEND

Frontend is complete only when:

-   Angular is NON-STANDALONE
-   module-based architecture is used
-   Bootstrap is used
-   professional sports/turf visual identity exists
-   one coherent color system is used
-   responsive design works
-   authentication works
-   JWT handling works
-   route guards work
-   role-aware navigation works
-   facility/turf interfaces work
-   booking interfaces work
-   forms work
-   field validation works
-   backend errors are handled
-   loading states exist
-   success states exist
-   AI turf recommendation UI works
-   AI slot recommendation UI works
-   AI demand forecast UI works
-   AI pricing UI works
-   owner approval UI works
-   AI errors are handled

------------------------------------------------------------------------

# 56. DEFINITION OF DONE --- END TO END

Final system must demonstrate:

``` text
Angular
 ↓
Spring Boot
 ↓
MySQL/domain services
 ↓
Ollama for AI
 ↓
validated backend result
 ↓
Angular
```

The following must be demonstrably functional:

-   authentication
-   authorization
-   existing booking workflows
-   centralized exception handling
-   frontend validation
-   AI turf recommendation
-   AI slot recommendation
-   AI demand forecasting
-   AI dynamic pricing
-   owner pricing approval
-   audit/history
-   Ollama failure handling

No critical compile/runtime errors may remain.

------------------------------------------------------------------------

# 57. NON-NEGOTIABLE RULES

1.  Phase 1 is complete and protected.
2.  Inspect actual source before modifying.
3.  Do not redesign working architecture without evidence.
4.  Do not invent IDs.
5.  Do not invent historical data.
6.  Do not invent AI features.
7.  AI scope is strictly SRS #8--#11.
8.  Do not build a chatbot.
9.  Do not hardcode AI responses.
10. Use real application data.
11. Deterministic backend rules override AI.
12. Never recommend unavailable resources.
13. Never bypass authorization.
14. Never exceed owner pricing limits.
15. Never automatically apply AI pricing.
16. Owner approval is mandatory.
17. Preserve auditability.
18. Do not expose secrets.
19. Externalize Ollama configuration.
20. Check dependency compatibility before adding libraries.
21. Do not silently ignore compile errors.
22. Do not silently ignore runtime errors.
23. Do not claim tests passed without evidence.
24. Angular MUST remain NON-STANDALONE.
25. Bootstrap is required.
26. Frontend field validation is required.
27. Backend validation remains authoritative.
28. Use one application-wide visual design system.
29. Do not create unrelated page color schemes.
30. Do not build a generic unstyled CRUD frontend.
31. Preserve existing database relationships.
32. Do not unnecessarily modify existing APIs.
33. Do not duplicate backend business logic in Angular.
34. Prefer minimal changes.
35. Compile after controlled changes.
36. Test before proceeding.
37. Document actual evidence.
38. Do not implement out-of-scope SRS AI features merely because the SRS
    lists them.
39. Never treat an LLM response as authoritative business data.
40. Never allow an AI recommendation to directly mutate a protected
    business state without deterministic authorization and validation.

------------------------------------------------------------------------

# 58. FINAL OBJECTIVE

The objective is not to generate the largest amount of code.

The objective is:

> **A stable, professional, working AI-Based Turf Management System that
> extends the existing Phase-1 backend safely and integrates JWT/Spring
> Security, Ollama, exactly four SRS AI capabilities, and an Angular
> NON-STANDALONE Bootstrap frontend end-to-end.**
