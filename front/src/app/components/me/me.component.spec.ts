import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    password: 'password123', // Ajout du password manquant
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: false,
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com'
    },
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        BrowserAnimationsModule,
        RouterTestingModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    // Reset mocks
    jest.clearAllMocks();

    // Mock le retour de getById par défaut
    mockUserService.getById.mockReturnValue(of(mockUser));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user details on init', () => {
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should handle error when loading user details fails', () => {
    // Remplacer le mock pour ce test spécifique
    mockUserService.getById.mockReturnValue(throwError(() => new Error('Failed to fetch user')));

    // Réinitialiser le composant pour déclencher ngOnInit avec le nouveau mock
    component.ngOnInit();

    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Error loading user', 'Close', { duration: 3000 });
  });

  it('should delete account and navigate to home when confirmed', () => {
    // Mock window.confirm to return true
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    mockUserService.delete.mockReturnValue(of({}));

    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);

    // Restore window.confirm
    jest.restoreAllMocks();
  });

  it('should not delete account when not confirmed', () => {
    // Mock window.confirm to return false
    jest.spyOn(window, 'confirm').mockReturnValue(false);

    component.delete();

    expect(mockUserService.delete).not.toHaveBeenCalled();

    // Restore window.confirm
    jest.restoreAllMocks();
  });

  it('should handle error when delete fails', () => {
    // Mock window.confirm to return true
    jest.spyOn(window, 'confirm').mockReturnValue(true);
    mockUserService.delete.mockReturnValue(throwError(() => new Error('Failed to delete')));

    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Error deleting account', 'Close', { duration: 3000 });

    // Restore window.confirm
    jest.restoreAllMocks();
  });
});
