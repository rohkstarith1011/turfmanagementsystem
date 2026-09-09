import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Auth } from '../../services/auth';
import { AuthRequestDTO } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  standalone: false
})
export class Login implements OnInit {
  model: AuthRequestDTO = { email: '', password: '' };
  loading = false;
  error = '';
  returnUrl = '/';

  constructor(
    private authService: Auth,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    }
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    
    this.authService.login(this.model).subscribe({
      next: (response) => {
        if (this.returnUrl !== '/') {
          this.router.navigate([this.returnUrl]);
        } else {
          // Route based on role
          const roles = response.roles || [];
          if (roles.includes('ROLE_ADMIN')) {
            this.router.navigate(['/admin-dashboard']);
          } else if (roles.includes('ROLE_OWNER')) {
            this.router.navigate(['/owner-dashboard']);
          } else {
            this.router.navigate(['/player-home']);
          }
        }
      },
      error: (error) => {
        this.error = error.error?.message || 'Login failed. Please check your credentials.';
        this.loading = false;
      }
    });
  }
}
