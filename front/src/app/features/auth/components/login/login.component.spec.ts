import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    // Reset mocks
    jest.clearAllMocks();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize login form', () => {
    expect(component.form).toBeDefined();
    expect(component.form.get('email')).toBeDefined();
    expect(component.form.get('password')).toBeDefined();
    expect(component.form.valid).toBeFalsy();
  });

  it('should validate form when email and password are provided', () => {
    component.form.setValue({
      email: 'test@example.com',
      password: 'password123'
    });
    expect(component.form.valid).toBeTruthy();
  });

  it('should show validation errors for invalid email', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('invalid-email');
    expect(emailControl?.valid).toBeFalsy();
    expect(emailControl?.hasError('email')).toBeTruthy();
  });

  it('should require email field', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();
    expect(emailControl?.hasError('required')).toBeTruthy();
  });

  it('should require password field', () => {
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();
    expect(passwordControl?.hasError('required')).toBeTruthy();
  });

  it('should toggle password visibility', () => {
    expect(component.hide).toBeTruthy();
    component.hide = false;
    expect(component.hide).toBeFalsy();
  });

  it('should call login service and navigate to sessions on successful login', () => {
    const loginRequest = {
      email: 'test@example.com',
      password: 'password123'
    };

    const sessionInfo: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'test',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    // Setup form
    component.form.setValue(loginRequest);

    // Mock successful login
    mockAuthService.login.mockReturnValue(of(sessionInfo));

    // Submit form
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith(loginRequest);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(sessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should display error message on login failure', () => {
    const loginRequest = {
      email: 'test@example.com',
      password: 'wrong-password'
    };

    // Setup form
    component.form.setValue(loginRequest);

    // Mock failed login
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));

    // Submit form
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith(loginRequest);
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});
