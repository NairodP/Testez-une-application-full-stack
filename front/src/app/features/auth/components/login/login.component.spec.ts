import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
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
import { By } from '@angular/platform-browser';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent (Tests d\'intégration)', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
  let httpTestingController: HttpTestingController;

  // MockSessionInfo utilisée dans les tests
  const sessionInfo: SessionInformation = {
    token: 'fake-token',
    type: 'Bearer',
    id: 1,
    username: 'test',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        AuthService,
        SessionService
      ],
      imports: [
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        HttpClientTestingModule,
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
    httpTestingController = TestBed.inject(HttpTestingController);

    // Espionner les méthodes pour vérifier les appels
    jest.spyOn(sessionService, 'logIn');
    jest.spyOn(router, 'navigate');

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

    // Setup form
    component.form.setValue(loginRequest);
    
    // Submit form
    component.submit();
    
    // Intercepter la requête HTTP
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest);
    
    // Simuler une réponse réussie
    req.flush(sessionInfo);
    
    // Vérifier les comportements attendus
    expect(sessionService.logIn).toHaveBeenCalledWith(sessionInfo);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
    
    // Vérifier qu'il n'y a pas de requêtes en attente
    httpTestingController.verify();
  });

  it('should display error message on login failure', () => {
    const loginRequest = {
      email: 'test@example.com',
      password: 'wrong-password'
    };

    // Setup form
    component.form.setValue(loginRequest);
    
    // Submit form
    component.submit();
    
    // Intercepter la requête HTTP
    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    
    // Simuler une erreur
    req.error(new ErrorEvent('Network error'));
    
    // Vérifier les comportements attendus
    expect(sessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
    
    // Vérifier qu'il n'y a pas de requêtes en attente
    httpTestingController.verify();
  });
});
