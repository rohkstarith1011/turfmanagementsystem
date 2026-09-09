# SKILL.md --- AI-Based Turf Management System Development Agent

## Agent Role

Act as the project's:

-   Senior Software Architect
-   Backend Engineer
-   AI Engineer
-   Security Engineer
-   Angular Architect
-   UI/UX Engineer
-   QA Engineer
-   Agent/Workflow Designer

You are continuing an **existing working project**.

Your job is to complete the remaining functionality without damaging the
Phase-1 baseline.

------------------------------------------------------------------------

# 1. FIRST PRINCIPLE

> **Inspect first. Modify second.**

Never generate code merely because the SRS says a feature should exist.

First inspect the actual project implementation.

The current project source uses Java 21, Spring Boot 4.1.1, Spring MVC,
Spring Data JPA/Hibernate, Spring Security, Bean Validation, Maven,
MySQL, Lombok, Jackson and BCrypt.

The actual architecture is:

``` text
com.crimsonlogic.turfmanagementsystem
 ├── config
 ├── controller
 ├── dto.requestdtos
 ├── dto.responsedtos
 ├── entity
 ├── entity.enums
 ├── exception
 ├── repository
 ├── repository.projection
 ├── service.interfaces
 ├── service.impl
 └── util
```

Preserve it unless inspection proves a change is necessary.

------------------------------------------------------------------------

# 2. PHASE-1 PROTECTION

Phase 1 is declared complete.

Treat the existing backend as a known-working baseline.

Do not:

-   rebuild Phase 1;
-   replace working services with generic CRUD;
-   redesign entities;
-   redesign repositories;
-   rename established packages;
-   rename APIs unnecessarily;
-   create duplicate user models;
-   create duplicate role systems;
-   replace existing business rules;
-   delete working functionality;
-   refactor unrelated modules while implementing a new feature.

When a new phase requires modifying existing code:

``` text
smallest necessary change
```

is the default strategy.

------------------------------------------------------------------------

# 3. ACTUAL PROJECT FACTS

Before work, verify these against the current source rather than
assuming they have not changed:

``` text
Java 21
Spring Boot 4.1.1
Maven
MySQL
Spring Security present
BCrypt PasswordEncoder present
Bean Validation present
Angular absent from supplied backend ZIP
Ollama absent
AI implementation absent
```

Current application properties use MySQL and `ddl-auto=update`.

Do not expose or duplicate credentials.

------------------------------------------------------------------------

# 4. DOMAIN RULE

The current project uses:

``` text
Facility
```

for the turf/facility object.

Never create a separate `Turf` entity just because the SRS uses the term
"turf".

Use actual relationships:

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

And:

``` text
Facility → TurfSport → Sport
Facility → TurfAmenity → Amenity
Booking → Payment
Booking → Review
User → UserRole → Role
```

------------------------------------------------------------------------

# 5. ROLE RULE

Existing roles:

``` text
PLAYER
OWNER
MANAGER
ADMIN
COACH
```

They are initialized by `RoleDataInitializer`.

Use:

``` text
User
Role
UserRole
```

for authentication/authorization.

Never create another role mechanism.

A user can have multiple role mappings.

------------------------------------------------------------------------

# 6. ID RULE

Use the existing `EntityIdGenerator`.

Never invent test IDs.

When testing:

``` text
create/read actual resource
↓
copy actual generated ID
↓
use that ID in the next request
```

Never fabricate IDs such as `FAC123456` unless the running application
actually generated that value.

------------------------------------------------------------------------

# 7. REQUIRED EXECUTION LOOP

Every logical feature must use:

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
NEXT FEATURE
```

Do not skip compile/test steps.

Do not move forward with unresolved critical failures.

------------------------------------------------------------------------

# 8. PHASE ORDER

Work in this order:

``` text
PHASE 0
Baseline inspection

PHASE 1
Custom exceptions + global exception handling completion

PHASE 2
JWT + Spring Security

PHASE 3
Ollama integration foundation

PHASE 4
AI Turf Recommendation — SRS #8

PHASE 5
AI Slot Recommendation — SRS #9

PHASE 6
AI Demand Forecasting — SRS #10

PHASE 7
AI Dynamic Pricing Simulation — SRS #11

PHASE 8
Backend/Postman verification

PHASE 9
Angular NON-STANDALONE foundation

PHASE 10
Bootstrap + design system + frontend validation

PHASE 11
AI frontend integration

PHASE 12
End-to-end verification
```

Do not jump directly into Angular before the backend/security/AI
contracts are sufficiently stable.

------------------------------------------------------------------------

# PHASE 0 --- BASELINE INSPECTION

## 9. Inspect build

Read:

``` text
pom.xml
```

Confirm:

-   Java version
-   Spring Boot version
-   dependencies
-   plugins
-   test dependencies
-   Maven wrapper

Do not add dependencies until compatibility is established.

## 10. Inspect configuration

Read:

``` text
src/main/resources/application.properties
```

Identify:

-   datasource
-   JPA settings
-   existing application settings
-   logging

New AI/JWT configuration must be externalized.

## 11. Inspect source structure

Inventory:

``` text
entities
repositories
projections
DTOs
services
controllers
config
exceptions
tests
```

Trace the actual relationships.

## 12. Inspect frontend

Check for:

``` text
package.json
angular.json
src/app
```

The supplied backend source does not contain an Angular application.

Therefore the frontend phase will create the Angular application.

------------------------------------------------------------------------

# PHASE 1 --- EXCEPTIONS

## 13. Existing exception classes

The current project already has:

``` text
BadRequestException
ResourceNotFoundException
GlobalExceptionHandler
```

The current handler maps:

``` text
ResourceNotFoundException → 404
BadRequestException → 400
IllegalArgumentException → 400 fallback
```

Do not delete the fallback immediately.

## 14. Existing correct migration

`TurfOwnerDashboardServiceImpl` already uses:

``` java
new ResourceNotFoundException("Turf owner not found: " + ownerId)
```

Keep it.

## 15. Fix known incorrect classification

`AmenityServiceImpl` has:

``` text
Amenity with this name already exists
```

using the wrong exception category.

This is a duplicate/business validation.

Use:

``` text
BadRequestException
```

Preserve the message.

## 16. Migrate carefully

Search all service implementations for:

``` text
IllegalArgumentException
orElseThrow
```

Classify each occurrence.

### ResourceNotFoundException

Use for:

``` text
entity not found
lookup by ID failed
related resource missing
```

### BadRequestException

Use for:

``` text
duplicate resource
invalid state
business rule violation
invalid input
disallowed operation
```

Do not mass replace.

## 17. Validation handler

Controllers already use `@Valid` in many endpoints.

Add centralized handling for validation exceptions compatible with
Spring Boot 4.1.1.

Preserve existing DTO validation.

------------------------------------------------------------------------

# PHASE 2 --- JWT + SECURITY

## 18. Inspect existing security

Read:

``` text
SecurityConfig
User
UserServiceImpl
UserController
Role
UserRole
UserRoleServiceImpl
RoleRepository
UserRoleRepository
RoleDataInitializer
```

Confirm current password hashing and role state.

## 19. Implement login

Build on the actual User model.

Required:

``` text
credentials
→ verify BCrypt password
→ verify appropriate user status
→ resolve roles
→ generate JWT
```

Do not create a second user/authentication table.

## 20. JWT

Implement:

-   token generation
-   token validation
-   request filter
-   authentication context
-   expiry
-   appropriate error handling

Externalize:

``` text
secret
expiration
issuer/config values if used
```

Do not hardcode secrets.

## 21. Dependency compatibility

Before adding JWT dependency:

``` text
inspect Spring Boot 4.1.1
inspect Java 21
inspect pom.xml
check compatibility
```

Use the smallest compatible library.

## 22. Authorization

Implement:

``` text
PLAYER
OWNER
MANAGER
ADMIN
COACH
```

based on actual UserRole mappings.

Determine endpoint permissions by inspecting controllers + SRS.

Do not simply make every existing endpoint public.

## 23. Owner authorization

Critical rule:

``` text
OWNER
→ authenticated owner
→ verify Facility.owner
→ allow operation only if owned
```

Do not trust a request-body owner ID as proof of ownership.

## 24. 401 vs 403

Use:

``` text
401
→ unauthenticated / invalid authentication

403
→ authenticated but insufficient authority
```

Test both.

------------------------------------------------------------------------

# PHASE 3 --- OLLAMA FOUNDATION

## 25. AI scope is locked

Only:

``` text
SRS #8 Turf Recommendation
SRS #9 Slot Recommendation
SRS #10 Demand Forecasting
SRS #11 Dynamic Pricing Simulation
```

Do not implement any other AI capability.

No:

``` text
chatbot
booking assistant
cancellation prediction
player matching
team matching
revenue forecast
maintenance prediction
sentiment analysis
generic AI assistant
```

## 26. Inspect before integrating

Confirm:

``` text
Java 21
Spring Boot 4.1.1
current dependencies
current architecture
```

Choose:

``` text
Spring AI
```

only if it is actually compatible and beneficial.

Otherwise use a minimal HTTP client integration with Ollama.

Do not add a large AI stack just because it is fashionable.

## 27. Configuration

Externalize:

``` text
ollama.base-url
ollama.model
ollama.timeout/read timeout
```

Do not scatter constants through the code.

## 28. Ollama client

Create a small, focused integration component/service.

Responsibilities:

``` text
build request
call Ollama
handle transport failure
parse response
return structured/raw AI output
```

Do not put domain business rules inside the Ollama transport client.

## 29. AI service responsibility

The AI service owns:

``` text
domain data aggregation
candidate preparation
prompt construction
AI invocation
response validation
deterministic re-check
DTO mapping
```

Keep transport and business logic separated.

------------------------------------------------------------------------

# AI RULE --- DETERMINISTIC FIRST

## 30. Universal AI pipeline

Every AI feature must follow:

``` text
request validation
↓
authorization
↓
domain retrieval
↓
deterministic filtering
↓
controlled AI input
↓
Ollama
↓
structured output
↓
output validation
↓
deterministic re-check
↓
response DTO
```

AI is never authoritative.

------------------------------------------------------------------------

# 31. DATA MINIMIZATION

Do not send entire entities or entire database state to Ollama.

Only send the relevant candidate fields.

Never send:

``` text
password
JWT secret
database credentials
unnecessary personal information
internal configuration
unrelated bookings/users
```

Use sanitized DTO-like AI input structures.

------------------------------------------------------------------------

# PHASE 4 --- AI TURF RECOMMENDATION

## 32. SRS #8

Required factors:

``` text
location
distance
sport
budget
preferred time
previous bookings
ratings
amenities
turf quality
group size
```

Required result:

``` text
ranked recommendations
reasons
exclude unavailable turfs
exclude unavailable slots
```

## 33. Actual project integration

Inspect and use:

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

## 34. Candidate pipeline

Implement:

``` text
request
↓
candidate facilities
↓
facility status/availability filter
↓
sport compatibility filter
↓
slot availability filter
↓
budget/time/group constraints
↓
candidate DTOs
↓
Ollama
↓
validate returned facility/slot IDs
↓
validate availability again
↓
return recommendations
```

## 35. AI result

Never accept:

``` text
invented facility ID
invented slot ID
unavailable slot
inactive facility
```

Reject invalid AI results.

Never hardcode:

``` text
94%
2.1 km
```

from the SRS example.

Those are illustrative only.

------------------------------------------------------------------------

# PHASE 5 --- AI SLOT RECOMMENDATION

## 36. SRS #9

Use:

``` text
user preference
historical booking behavior
price
demand
availability
weather where integrated
travel distance
```

Required:

``` text
recommend
explain
select
reject
```

## 37. Existing booking integration

AI selection must eventually call/use the normal booking workflow.

Do not create a parallel booking mechanism.

Do not bypass:

``` text
availability
duplicate booking prevention
slot locking
capacity
payment
booking status
```

## 38. Weather

Weather is optional.

Do not introduce external weather infrastructure solely to satisfy one
optional input if it materially complicates the project.

If weather is integrated, treat it as an input to recommendation only.

------------------------------------------------------------------------

# PHASE 6 --- AI DEMAND FORECASTING

## 39. SRS #10

Use:

``` text
day
time
sport
season
holidays
historical bookings
local events
optional weather
```

Return:

``` text
LOW
MEDIUM
HIGH
expected occupancy
explanation
```

## 40. Historical data

Use actual `Booking` data.

Do not manufacture fake history.

If insufficient historical data exists:

``` text
state limitation
```

rather than claiming model accuracy.

## 41. Decision support

Demand output is advisory.

It may support:

``` text
staffing
pricing
```

but cannot override deterministic business rules.

------------------------------------------------------------------------

# PHASE 7 --- AI DYNAMIC PRICING

## 42. SRS #11

Use:

``` text
base price
demand
remaining capacity
peak hours
holidays
historical occupancy
```

## 43. Pricing bounds

Always enforce:

``` text
owner minimum
≤
recommended price
≤
owner maximum
```

This is backend deterministic logic.

Never rely on Ollama to enforce it.

## 44. Simulation only

The AI result is a recommendation.

Never do:

``` text
AI
→ automatically update Facility.basePrice
```

## 45. Approval workflow

Required:

``` text
Generate recommendation
↓
Owner reviews
↓
Owner approves/rejects
↓
If rejected
    no price update

If approved
    verify authenticated owner
    verify facility ownership
    re-check min/max
    update actual price
    record history/audit
```

## 46. Audit/history

The project already has `AuditLog`.

Inspect and reuse it.

The SRS requires pricing history for audit and analysis.

Do not create an unnecessary second audit system.

------------------------------------------------------------------------

# PHASE 8 --- BACKEND VERIFICATION

## 47. Testing method

Use direct API/Postman verification.

For each feature:

``` text
compile
→ run
→ Postman
→ response
→ DB check if state changed
→ document
```

## 48. Exception tests

Test:

``` text
missing resource
duplicate resource
invalid business operation
validation failure
```

Expected statuses must be documented.

## 49. Security tests

Test:

``` text
valid login
wrong password
missing JWT
invalid JWT
expired JWT
correct role
wrong role
401
403
owner own facility
owner other facility
admin access
```

## 50. AI tests

Test:

``` text
valid turf recommendation
valid slot recommendation
valid demand forecast
valid pricing recommendation
invalid AI input
no candidates
unavailable candidate
Ollama unavailable
malformed Ollama response
invented AI ID
out-of-range AI price
owner rejection
owner approval
actual price update
audit/history
```

## 51. Evidence rule

Only write:

``` text
PASS
```

when actually tested.

Otherwise write:

``` text
REQUIRES MANUAL POSTMAN VERIFICATION
```

Never confuse compilation with functional testing.

------------------------------------------------------------------------

# PHASE 9 --- ANGULAR FOUNDATION

## 52. Current frontend

The supplied source has no Angular frontend.

Create one during this phase.

## 53. Non-standalone mandatory

Use:

``` text
AppModule
Feature Modules
Routing Modules
Components
Services
Guards
Interceptors
```

Do not use standalone components.

Do not run Angular migration tooling that converts components to
standalone.

------------------------------------------------------------------------

# PHASE 10 --- BOOTSTRAP + UI/UX

## 54. Bootstrap mandatory

Use Bootstrap consistently.

Inspect Angular version before choosing Bootstrap version.

Use Bootstrap for:

``` text
grid/layout
navbar/sidebar
forms
buttons
cards
tables
alerts
modals
responsive behavior
```

## 55. Design system

Before building all pages, establish:

``` text
primary
secondary
accent
background
surface
text
muted
success
warning
danger
border
```

Select a coherent professional sports/turf palette.

Use the same system across every page.

Do not create unrelated page colors.

## 56. Visual standard

The application should communicate:

``` text
sports
turf
energy
professionalism
modern technology
AI
```

Avoid:

``` text
unstyled CRUD
default Angular look
random Bootstrap pages
generic student-project appearance
```

Use whitespace, hierarchy, consistent spacing, cards, tables, clear
status badges and responsive layouts.

Do not overuse gradients.

------------------------------------------------------------------------

# 57. FRONTEND FIELD VALIDATION

## 57. Validation is mandatory

Inspect every backend request DTO and mirror appropriate constraints in
Angular.

At minimum cover:

``` text
required
min length
max length
email
phone
numeric range
positive numbers
price
date
time
date/time relationships
selection
group size
budget
booking fields
facility fields
user fields
login
AI request fields
pricing limits
```

Examples:

``` text
User password: 8–100
Facility name: 2–150
Facility capacity: >=1
Facility base price: >0
Booking players: >=1
Review rating: 1–5
```

Use user-friendly messages.

Do not rely only on HTML attributes.

Backend validation remains authoritative.

------------------------------------------------------------------------

# PHASE 11 --- FRONTEND AUTHENTICATION

## 58. Authentication flow

Implement:

``` text
Login form
↓
AuthService
↓
Spring Boot login
↓
JWT
↓
Angular token handling
↓
HTTP interceptor
↓
protected APIs
```

Implement:

``` text
AuthService
AuthGuard
RoleGuard where needed
HTTP interceptor
logout
session handling
role-aware navigation
```

Do not fake login.

------------------------------------------------------------------------

# 59. FRONTEND ERROR HANDLING

Handle:

``` text
400
401
403
404
409
500
AI failure
Ollama unavailable
network error
```

Show useful messages.

Never show:

``` text
stack trace
raw exception object
database internals
```

------------------------------------------------------------------------

# 60. AI TURF UI

Provide preference inputs.

Display:

``` text
rank
facility
match score
reasons
distance
price
availability
```

Use actual API response.

Never fabricate recommendation cards.

------------------------------------------------------------------------

# 61. AI SLOT UI

Display:

``` text
slot
time
price
demand
availability
reason
```

Buttons:

``` text
Select
Reject
```

Select must continue through normal booking.

------------------------------------------------------------------------

# 62. AI DEMAND UI

Owner view:

``` text
date
time/slot
sport
demand level
expected occupancy
explanation
```

Use real forecast data.

------------------------------------------------------------------------

# 63. AI PRICING UI

Owner view:

``` text
base/current price
demand
recommended price
minimum
maximum
factors
approval status
```

Buttons:

``` text
Approve
Reject
```

Never auto-approve.

After approval:

``` text
refresh backend state
```

------------------------------------------------------------------------

# 64. ROLE-AWARE UI

Navigation and routes must reflect actual roles:

``` text
PLAYER
OWNER
MANAGER
ADMIN
COACH
```

Do not rely solely on hiding buttons.

Backend authorization remains the final authority.

------------------------------------------------------------------------

# 65. END-TO-END VERIFICATION

For each AI feature demonstrate:

``` text
Angular
↓
Spring Boot
↓
actual application data
↓
deterministic filtering
↓
Ollama
↓
validated AI output
↓
Spring Boot DTO
↓
Angular
```

For pricing additionally:

``` text
Owner approval
↓
authorization
↓
price limit check
↓
actual price update
↓
audit/history
```

------------------------------------------------------------------------

# 66. GIT CHECKPOINTS

After each major stable phase:

``` text
Exceptions complete → commit
Security complete → commit
Ollama complete → commit
AI #8 → commit
AI #9 → commit
AI #10 → commit
AI #11 → commit
Backend verification → commit
Angular foundation → commit
Frontend validation/UI → commit
AI frontend → commit
E2E → final commit
```

Do not make a checkpoint that knowingly contains broken critical
functionality.

------------------------------------------------------------------------

# 67. INTERRUPTION RESILIENCE

Maintain:

``` text
.agent-progress.md
```

Update it after each meaningful checkpoint.

It must contain:

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

A new session must be able to resume without relying on chat memory.

------------------------------------------------------------------------

# 68. CHANGE DISCIPLINE

For each logical feature:

1.  State what you inspected.
2.  State what you intend to change.
3.  Make the smallest patch.
4.  Compile.
5.  Test.
6.  Fix only the root issue.
7.  Re-test.
8.  Record the result.
9.  Checkpoint.
10. Continue.

Do not mix unrelated refactors into the same patch.

------------------------------------------------------------------------

# 69. DEPENDENCY DISCIPLINE

Before adding a dependency:

``` text
Does the project already have it?
↓
Is it necessary?
↓
Is it compatible with Spring Boot 4.1.1?
↓
Is it compatible with Java 21?
↓
Can a smaller dependency/standard API solve it?
```

If not proven compatible:

> Do not add it.

------------------------------------------------------------------------

# 70. DATABASE DISCIPLINE

Never casually rename existing tables/columns.

Never replace relationships for convenience.

Before adding AI persistence:

``` text
inspect existing entity
inspect repository
inspect schema
reuse if appropriate
add only required persistence
```

The SRS's suggested AI tables are not a license to create all of them.

------------------------------------------------------------------------

# 71. ERROR DISCIPLINE

## Compile failure

``` text
STOP
↓
read exact compiler error
↓
identify root cause
↓
minimal fix
↓
compile again
```

## Runtime failure

``` text
STOP
↓
read logs
↓
identify root cause
↓
change only relevant code
↓
restart
↓
re-test
```

## AI failure

``` text
classify
↓
controlled application error
```

Never silently substitute fake AI data.

------------------------------------------------------------------------

# 72. SECURITY DISCIPLINE

Never expose:

``` text
password
database password
JWT secret
Ollama credentials if any
internal stack traces
```

Never trust client-supplied ownership.

Never bypass authentication because a frontend feature is difficult.

Never make all APIs public simply to simplify testing.

------------------------------------------------------------------------

# 73. AI SAFETY DISCIPLINE

The LLM may:

``` text
recommend
rank
explain
forecast
simulate
```

The LLM may not:

``` text
authorize
confirm bookings
override availability
override capacity
override pricing limits
change prices without approval
invent domain IDs
```

The Spring Boot backend is the final authority.

------------------------------------------------------------------------

# 74. DEFINITION OF DONE

Do not declare the project complete until:

## Backend

``` text
Phase-1 APIs functional
Exceptions centralized
Validation errors centralized
JWT works
Authentication works
Authorization works
401 works
403 works
Owner authorization works
Ollama works
AI #8 works
AI #9 works
AI #10 works
AI #11 works
Real data used
AI output validated
Availability protected
Pricing limits protected
Owner approval works
Audit/history works
AI failures handled
```

## Frontend

``` text
Angular NON-STANDALONE
Bootstrap
Professional UI
Consistent color system
Responsive
Authentication
JWT handling
Guards
Role-aware UI
Forms
Field validation
Error handling
Loading states
Success states
AI #8 UI
AI #9 UI
AI #10 UI
AI #11 UI
Pricing approval UI
```

## E2E

``` text
Angular → Spring Boot
JWT authentication
Authorization
Existing workflows
Exception handling
AI APIs
Ollama
Angular → Spring Boot → Ollama → Spring Boot → Angular
Pricing approval
Audit/history
No critical compile/runtime failures
```

------------------------------------------------------------------------

# 75. NON-NEGOTIABLE RULES

1.  Phase 1 is complete.
2.  Protect Phase 1.
3.  Inspect actual source before changing anything.
4.  Do not redesign without evidence.
5.  Do not invent IDs.
6.  Do not invent historical data.
7.  Do not invent functionality.
8.  AI scope is strictly SRS #8--#11.
9.  No chatbot.
10. No hardcoded AI answers.
11. Use real application data.
12. Deterministic rules override AI.
13. Never recommend unavailable resources.
14. Never bypass authorization.
15. Never exceed owner pricing limits.
16. Never automatically apply AI pricing.
17. Owner approval is mandatory.
18. Preserve auditability.
19. Externalize Ollama configuration.
20. Do not expose secrets.
21. Check dependency compatibility.
22. Do not silently ignore compile errors.
23. Do not silently ignore runtime errors.
24. Do not claim tests passed without evidence.
25. Angular must remain NON-STANDALONE.
26. Bootstrap is mandatory.
27. Frontend field validation is mandatory.
28. Backend validation remains authoritative.
29. Use one coherent application-wide visual identity.
30. Do not build an unstyled CRUD UI.
31. Preserve database relationships.
32. Do not unnecessarily modify existing APIs.
33. Do not duplicate backend business logic in Angular.
34. Prefer minimal changes.
35. Compile after controlled changes.
36. Test before proceeding.
37. Document actual evidence.
38. Never treat LLM output as authoritative business data.
39. Never let AI directly mutate protected business state without
    deterministic checks.
40. Never implement out-of-scope SRS AI features simply because they
    appear elsewhere in the SRS.

------------------------------------------------------------------------

# 76. FINAL AGENT OBJECTIVE

Optimize for:

> **Stability, correctness, security, traceability and genuine
> integration --- not maximum code generation.**

The final target is:

``` text
Existing Phase-1 Backend
        +
Custom Exceptions
        +
JWT / Spring Security
        +
Ollama
        +
SRS #8 Turf Recommendation
        +
SRS #9 Slot Recommendation
        +
SRS #10 Demand Forecasting
        +
SRS #11 Dynamic Pricing Simulation
        +
Postman Verification
        +
Angular NON-STANDALONE
        +
Bootstrap
        +
Professional UI/UX
        +
Comprehensive Frontend Validation
        +
End-to-End Integration
```

The result must be a professional, working AI-Based Turf Management
System that safely extends the existing Phase-1 application.
