import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { Observable, of, throwError } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let activatedRoute: ActivatedRoute;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn(),
    delete: jest.fn()
  };

  const mockTeacherService = {
    detail: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockSession: Session = {
    id: 1,
    name: 'Session 1',
    description: 'Description 1',
    date: new Date(),
    teacher_id: 2,
    users: [1, 3],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeacher = {
    id: 2,
    firstName: 'Hélène',
    lastName: 'THIERCELIN',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1'
              }
            }
          }
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // Ajouter ce schéma pour gérer les erreurs liées à mat-card, mat-icon, etc.
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);

    // Reset mocks
    jest.clearAllMocks();
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load session and teacher details on init', () => {
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('2');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should handle error when loading session details fails', () => {
    // Réinitialiser le composant avant le test
    component.session = undefined;

    // Configurer la mock pour retourner une erreur
    mockSessionApiService.detail.mockReturnValue(throwError(() => new Error('Failed to load session')));

    component.ngOnInit();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    // Dans l'implémentation actuelle, session reste undefined quand il y a une erreur
    expect(component.session).toBeUndefined();
  });

  it('should handle error when loading teacher details fails', () => {
    // Réinitialiser le composant avant le test
    component.teacher = undefined;

    // Configurer les mocks pour retourner succès pour session mais erreur pour teacher
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(throwError(() => new Error('Failed to load teacher')));

    component.ngOnInit();

    expect(mockTeacherService.detail).toHaveBeenCalledWith('2');
    // Dans l'implémentation actuelle, teacher reste undefined quand il y a une erreur
    expect(component.teacher).toBeUndefined();
  });

  it('should check if user is participating in session', () => {
    // Current user ID is 1 based on mockSessionService
    expect(component.isParticipate).toBe(true); // users: [1, 3]

    // Créer une nouvelle session où l'utilisateur ne participe pas
    const nonParticipatingSession: Session = {
      ...mockSession,
      users: [2, 3] // User 1 is not participating
    };

    // Mettre à jour la mock pour retourner cette session
    mockSessionApiService.detail.mockReturnValue(of(nonParticipatingSession));

    // Appeler la méthode qui recalcule isParticipate
    component.ngOnInit();

    // La propriété isParticipate devrait être false après recalcul
    expect(component.isParticipate).toBe(false);
  });

  it('should participate in a session', () => {
    mockSessionApiService.participate.mockReturnValue(of({}));
    mockSessionApiService.detail.mockReturnValue(of(mockSession)); // This will be called to refresh

    component.participate();

    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should handle error when participating fails', () => {
    mockSessionApiService.participate.mockReturnValue(throwError(() => new Error('Failed to participate')));

    component.participate();

    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
  });

  it('should unparticipate from a session', () => {
    // User 1 is already participating in mockSession

    mockSessionApiService.unParticipate.mockReturnValue(of({}));
    mockSessionApiService.detail.mockReturnValue(of({
      ...mockSession,
      users: [3] // User 1 has been removed
    }));

    component.unParticipate();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
  });

  it('should handle error when unparticipating fails', () => {
    mockSessionApiService.unParticipate.mockReturnValue(throwError(() => new Error('Failed to unparticipate')));

    component.unParticipate();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
  });

  it('should navigate back', () => {
    // Mock window.history.back
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

    component.back();

    expect(spy).toHaveBeenCalled();

    // Restore original implementation
    spy.mockRestore();
  });

  it('should delete a session', () => {
    mockSessionApiService.delete.mockReturnValue(of({}));

    component.delete();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});

