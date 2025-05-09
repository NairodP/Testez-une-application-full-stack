import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from '../../../../interfaces/teacher.interface';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionId = '1';
  
  const mockSession: Session = {
    id: 1,
    name: 'Yoga Session',
    description: 'A relaxing yoga session',
    date: new Date(),
    teacher_id: 2,
    users: [1, 3], // User 1 participe déjà
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeacher: Teacher = {
    id: 2,
    lastName: 'Smith',
    firstName: 'Jane',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
      firstName: 'Admin',
      lastName: 'User',
      email: 'admin@example.com'
    }
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: mockSessionId })
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    // Reset mocks
    jest.clearAllMocks();
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component with correct session ID and admin status', () => {
    expect(component.sessionId).toBe(mockSessionId);
    expect(component.isAdmin).toBe(true);
    expect(component.userId).toBe('1');
  });

  it('should load session and teacher details on init', () => {
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('2');
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
    expect(component.isParticipate).toBe(true); // Car l'ID de l'utilisateur (1) est dans la liste des utilisateurs
  });

  it('should handle error when loading session details fails', () => {
    // Réinitialiser le composant pour ce test spécifique
    component.session = undefined;
    
    // Configurer la mock pour retourner une erreur
    mockSessionApiService.detail.mockReturnValue(throwError(() => new Error('Failed to load session')));
    
    component.ngOnInit();
    
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    // Dans l'implémentation actuelle, la session reste undefined en cas d'erreur
    expect(component.session).toBeUndefined();
  });

  it('should handle error when loading teacher details fails', () => {
    // Réinitialiser le composant pour ce test spécifique
    component.teacher = undefined;
    
    // Configurer les mocks pour retourner un succès pour la session mais une erreur pour l'enseignant
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(throwError(() => new Error('Failed to load teacher')));
    
    component.ngOnInit();
    
    expect(mockTeacherService.detail).toHaveBeenCalledWith('2');
    // Dans l'implémentation actuelle, teacher reste undefined en cas d'erreur
    expect(component.teacher).toBeUndefined();
  });

  it('should delete session and navigate to sessions list', () => {
    component.delete();
    
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should handle error when deleting session fails', () => {
    // Configurer la mock pour retourner une erreur lors de la suppression
    mockSessionApiService.delete.mockReturnValue(throwError(() => new Error('Failed to delete session')));
    
    component.delete();
    
    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    // Dans l'implémentation actuelle, il n'y a pas de gestion des erreurs pour la méthode delete
    // donc on ne peut pas tester le comportement en cas d'erreur
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should unparticipate from a session', () => {
    component.unParticipate();
    
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    // Après unParticipate, fetchSession est appelée
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  });

  it('should participate in a session', () => {
    // D'abord, faisons comme si l'utilisateur ne participe pas déjà
    const sessionWithoutUser = {
      ...mockSession,
      users: [3] // Remove user 1 from participants
    };
    
    mockSessionApiService.detail.mockReturnValueOnce(of(sessionWithoutUser));
    component.ngOnInit();
    expect(component.isParticipate).toBe(false);
    
    // Maintenant testons la participation
    mockSessionApiService.detail.mockReturnValueOnce(of(mockSession)); // Retour à la session avec l'utilisateur
    component.participate();
    
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
    // Après participate, fetchSession est appelée
    expect(mockSessionApiService.detail).toHaveBeenCalled();
    expect(component.isParticipate).toBe(true);
  });

  it('should navigate back when back function is called', () => {
    // Mock window.history.back
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    
    component.back();
    
    expect(historySpy).toHaveBeenCalled();
    
    // Restore window.history.back
    historySpy.mockRestore();
  });
});

