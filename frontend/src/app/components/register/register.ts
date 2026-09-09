import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { UserRequestDTO } from '../../models/user.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.html',
  styleUrls: ['./register.css'],
  standalone: false
})
export class Register implements OnInit {
  model: UserRequestDTO = { name: '', email: '', phone: '', password: '' };
  loading = false;
  error = '';

  constructor(
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    }
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    
    this.authService.register(this.model).subscribe({
      next: () => {
        // Automatically login after successful registration or navigate to login page
        // For simplicity, we navigate to login page with a success query param
        this.router.navigate(['/login'], { queryParams: { registered: true } });
      },
      error: (error) => {
        this.error = error.error?.message || 'Registration failed. Please try again.';
        this.loading = false;
      }
    });
  }
}
