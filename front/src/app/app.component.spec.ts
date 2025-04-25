import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('AppComponent', () => {
  let component: AppComponent;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockAuthService = {
    logout: jest.fn()
  };

  const mockSessionService = {
    $isLogged: jest.fn().mockReturnValue(of(false)),
    logOut: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Permet d'ignorer les erreurs liées aux composants non reconnus
    }).compileComponents();

    const fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    // Reset mocks
    jest.clearAllMocks();

    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should check if user is logged in via session service', () => {
    mockSessionService.$isLogged.mockReturnValue(of(true));

    // Call the method
    const isLogged$ = component.$isLogged();

    // Subscribe to the observable
    isLogged$.subscribe((value) => {
      expect(value).toBeTruthy();
    });

    expect(mockSessionService.$isLogged).toHaveBeenCalled();
  });

  it('should handle logout', () => {
    // Call logout method
    component.logout();

    // Verify session service's logout was called
    expect(mockSessionService.logOut).toHaveBeenCalled();

    // Verify navigation to home page
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
