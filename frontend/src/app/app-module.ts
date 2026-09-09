import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Header } from './components/header/header';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { JwtInterceptor } from './interceptors/jwt-interceptor';
import { AdminDashboard } from './components/dashboards/admin-dashboard/admin-dashboard';
import { OwnerDashboard } from './components/dashboards/owner-dashboard/owner-dashboard';
import { PlayerHome } from './components/dashboards/player-home/player-home';
import { FacilityList } from './components/facilities/facility-list/facility-list';
import { FacilityCreate } from './components/facilities/facility-create/facility-create';
import { FacilitySearch } from './components/facilities/facility-search/facility-search';
import { FacilityView } from './components/facilities/facility-view/facility-view';
import { SlotBooking } from './components/bookings/slot-booking/slot-booking';
import { MyBookings } from './components/bookings/my-bookings/my-bookings';
import { SlotManagement } from './components/facilities/slot-management/slot-management';
import { FacilityImages } from './components/facilities/facility-images/facility-images';
import { AiTurfRecommendation } from './components/ai/ai-turf-recommendation/ai-turf-recommendation';
import { AiSlotRecommendation } from './components/ai/ai-slot-recommendation/ai-slot-recommendation';
import { AiDemandForecast } from './components/ai/ai-demand-forecast/ai-demand-forecast';
import { AiPricingSimulation } from './components/ai/ai-pricing-simulation/ai-pricing-simulation';
import { CoachList } from './components/coaches/coach-list/coach-list';
import { CoachingClassView } from './components/coaches/coaching-class-view/coaching-class-view';
import { TeamList } from './components/teams/team-list/team-list';
import { TeamCreate } from './components/teams/team-create/team-create';
import { ReviewSubmit } from './components/reviews/review-submit/review-submit';
import { UserList } from './components/admin/user-list/user-list';
import { AdminFacilityList } from './components/admin/admin-facility-list/admin-facility-list';

@NgModule({
  declarations: [
    App,
    Header,
    Login,
    Register,
    AdminDashboard,
    OwnerDashboard,
    PlayerHome,
    FacilityList,
    FacilityCreate,
    FacilitySearch,
    FacilityView,
    SlotBooking,
    MyBookings,
    SlotManagement,
    FacilityImages,
    AiTurfRecommendation,
    AiSlotRecommendation,
    AiDemandForecast,
    AiPricingSimulation,
    CoachList,
    CoachingClassView,
    TeamList,
    TeamCreate,
    ReviewSubmit,
    UserList,
    AdminFacilityList
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [App]
})
export class AppModule { }
