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
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent (Tests d\'intégration)', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        AuthService
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
    // Obtenir le contrôleur HTTP pour intercepter les requêtes
    httpTestingController = TestBed.inject(HttpTestingController);
    
    // Espionner la méthode de navigation du router
    jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));
    
    // Remplir le formulaire avec des données valides
    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    // Soumettre le formulaire
    component.submit();

    // Intercepter et répondre à la requête HTTP
    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    
    // Simuler une réponse réussie
    req.flush(null);
    
    // Vérifier les comportements attendus
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
    
    // Vérifier qu'il n'y a pas de requêtes en attente
    httpTestingController.verify();
  });

  it('should handle errors on registration failure', () => {
    // Obtenir le contrôleur HTTP pour intercepter les requêtes
    httpTestingController = TestBed.inject(HttpTestingController);
    
    // Espionner la méthode de navigation du router
    jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));
    
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
    
    // Intercepter la requête HTTP et simuler une erreur
    const req = httpTestingController.expectOne('api/auth/register');
    req.error(new ErrorEvent('Network error'), { status: 500 });
    
    // Vérifier les comportements attendus
    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
    
    // Vérifier qu'il n'y a pas de requêtes en attente
    httpTestingController.verify();
  });
});
