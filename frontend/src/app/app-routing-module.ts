import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { AdminDashboard } from './components/dashboards/admin-dashboard/admin-dashboard';
import { OwnerDashboard } from './components/dashboards/owner-dashboard/owner-dashboard';
import { PlayerHome } from './components/dashboards/player-home/player-home';
import { FacilityList } from './components/facilities/facility-list/facility-list';
import { FacilityCreate } from './components/facilities/facility-create/facility-create';
import { FacilityImages } from './components/facilities/facility-images/facility-images';
import { SlotManagement } from './components/facilities/slot-management/slot-management';
import { AiTurfRecommendation } from './components/ai/ai-turf-recommendation/ai-turf-recommendation';
import { AiSlotRecommendation } from './components/ai/ai-slot-recommendation/ai-slot-recommendation';
import { AiDemandForecast } from './components/ai/ai-demand-forecast/ai-demand-forecast';
import { AiPricingSimulation } from './components/ai/ai-pricing-simulation/ai-pricing-simulation';
import { FacilitySearch } from './components/facilities/facility-search/facility-search';
import { FacilityView } from './components/facilities/facility-view/facility-view';
import { MyBookings } from './components/bookings/my-bookings/my-bookings';
import { AuthGuard } from './guards/auth-guard';
import { CoachList } from './components/coaches/coach-list/coach-list';
import { CoachingClassView } from './components/coaches/coaching-class-view/coaching-class-view';
import { TeamList } from './components/teams/team-list/team-list';
import { TeamCreate } from './components/teams/team-create/team-create';
import { ReviewSubmit } from './components/reviews/review-submit/review-submit';
import { UserList } from './components/admin/user-list/user-list';
import { AdminFacilityList } from './components/admin/admin-facility-list/admin-facility-list';

const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  
  // Dashboards
  { 
    path: 'admin-dashboard', 
    component: AdminDashboard, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_ADMIN'] } 
  },
  { 
    path: 'owner-dashboard', 
    component: OwnerDashboard, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER'] } 
  },
  { 
    path: 'player-home', 
    component: PlayerHome, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },

  // Owner Facilities Management
  { 
    path: 'facilities', 
    component: FacilityList, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER', 'ROLE_MANAGER'] } 
  },
  { 
    path: 'facilities/create', 
    component: FacilityCreate, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER'] } 
  },
  { 
    path: 'facilities/:id/slots', 
    component: SlotManagement, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER', 'ROLE_MANAGER'] } 
  },
  { 
    path: 'facilities/:id/images', 
    component: FacilityImages, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER', 'ROLE_MANAGER'] } 
  },

  // Player Booking Flow
  { 
    path: 'player/search', 
    component: FacilitySearch, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },
  { 
    path: 'player/facilities/:id', 
    component: FacilityView, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },
  { 
    path: 'player/bookings', 
    component: MyBookings, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },

  // AI Routes
  { 
    path: 'ai/turf-recommendation', 
    component: AiTurfRecommendation, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },
  { 
    path: 'ai/slot-recommendation', 
    component: AiSlotRecommendation, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_PLAYER'] } 
  },
  { 
    path: 'ai/demand-forecast', 
    component: AiDemandForecast, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER', 'ROLE_MANAGER'] } 
  },
  { 
    path: 'ai/pricing-simulation', 
    component: AiPricingSimulation, 
    canActivate: [AuthGuard], 
    data: { roles: ['ROLE_OWNER', 'ROLE_MANAGER'] } 
  },

  // Coaches & Classes
  { path: 'coaches', component: CoachList },
  { path: 'coaching-classes/:id', component: CoachingClassView },

  // Teams
  { path: 'teams', component: TeamList, canActivate: [AuthGuard], data: { roles: ['ROLE_PLAYER'] } },
  { path: 'teams/create', component: TeamCreate, canActivate: [AuthGuard], data: { roles: ['ROLE_PLAYER'] } },

  // Reviews
  { path: 'reviews/submit/:bookingId', component: ReviewSubmit, canActivate: [AuthGuard], data: { roles: ['ROLE_PLAYER'] } },

  // Admin Modules
  { path: 'admin/users', component: UserList, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'admin/facilities', component: AdminFacilityList, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] } },

  // Home route placeholder
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  // Catch all
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
