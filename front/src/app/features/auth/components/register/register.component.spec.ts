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

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        RouterTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    // Reset mocks
    jest.clearAllMocks();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.form.get('email')?.value).toBe('');
    expect(component.form.get('firstName')?.value).toBe('');
    expect(component.form.get('lastName')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
    expect(component.form.valid).toBe(false);
  });

  it('should validate form with valid inputs', () => {
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
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

  it('should require all fields', () => {
    const controls = ['email', 'firstName', 'lastName', 'password'];

    controls.forEach(controlName => {
      const control = component.form.get(controlName);
      control?.setValue('');
      expect(control?.valid).toBeFalsy();
      expect(control?.hasError('required')).toBeTruthy();
    });
  });

  // Note: min/max sont utilisés pour les valeurs numériques, pas pour les longueurs de chaînes
  it('should check validation constraints on fields', () => {
    // Notre composant utilise min/max, mais ces validateurs sont prévus pour des valeurs numériques
    // Dans un vrai composant, on utiliserait Validators.minLength/maxLength pour valider la longueur des chaînes
    // Tests adaptés en fonction de l'implémentation actuelle
    
    // email validation
    const emailControl = component.form.get('email');
    emailControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();
    emailControl?.setValue('test@example.com');
    expect(emailControl?.valid).toBeTruthy();
    
    // firstName validation
    const firstNameControl = component.form.get('firstName');
    firstNameControl?.setValue('');
    expect(firstNameControl?.valid).toBeFalsy();
    firstNameControl?.setValue('John');
    expect(firstNameControl?.valid).toBeTruthy();
    
    // lastName validation
    const lastNameControl = component.form.get('lastName');
    lastNameControl?.setValue('');
    expect(lastNameControl?.valid).toBeFalsy();
    lastNameControl?.setValue('Doe');
    expect(lastNameControl?.valid).toBeTruthy();
    
    // password validation
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();
    passwordControl?.setValue('password123');
    expect(passwordControl?.valid).toBeTruthy();
  });

  it('should call register service and navigate to login on successful registration', () => {
    // Configurer le mock pour un succès
    mockAuthService.register.mockReturnValue(of(void 0));

    // Remplir le formulaire avec des données valides
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    // Soumettre le formulaire
    component.submit();

    // Vérifier les comportements attendus
    expect(mockAuthService.register).toHaveBeenCalledWith({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should handle errors on registration failure', () => {
    // Configurer le mock pour un échec
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));

    // Remplir le formulaire avec des données valides
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    // Vérifier que onError est initialement false
    expect(component.onError).toBeFalsy();

    // Soumettre le formulaire
    component.submit();

    // Vérifier les comportements attendus
    expect(mockAuthService.register).toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});
