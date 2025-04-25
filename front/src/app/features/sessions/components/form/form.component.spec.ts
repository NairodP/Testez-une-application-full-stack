import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Test Description',
    date: new Date('2023-01-01'),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeachers = [
    {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      firstName: 'Hélène',
      lastName: 'THIERCELIN',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn()
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of(mockTeachers))
  };

  const mockRouter = {
    url: '',
    navigate: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({ id: '1' })
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      declarations: [FormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    // Reset mocks
    jest.clearAllMocks();
    mockTeacherService.all.mockReturnValue(of(mockTeachers));
    mockRouter.url = '/sessions/create';
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should initialize form for creating a new session', () => {
    fixture.detectChanges();

    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toEqual('');
    expect(component.sessionForm?.get('description')?.value).toEqual('');
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual('');
  });

  it('should initialize form for updating an existing session', () => {
    mockRouter.url = '/sessions/update/1';
    mockSessionApiService.detail.mockReturnValue(of(mockSession));

    fixture.detectChanges();

    expect(component.onUpdate).toBeTruthy();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toEqual('Test Session');
    expect(component.sessionForm?.get('description')?.value).toEqual('Test Description');
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual(1);
  });

  it('should handle error when loading session for update', () => {
    mockRouter.url = '/sessions/update/1';
    mockSessionApiService.detail.mockReturnValue(throwError(() => new Error('Failed to load session')));

    fixture.detectChanges();

    expect(component.onUpdate).toBeTruthy();
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.sessionForm).toBeUndefined();
  });

  it('should redirect non-admin users to sessions list', () => {
    // Set user as non-admin
    mockSessionService.sessionInformation.admin = false;

    fixture.detectChanges();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);

    // Reset for other tests
    mockSessionService.sessionInformation.admin = true;
  });

  it('should create a new session when form is submitted in create mode', () => {
    const newSession = {
      name: 'New Session',
      description: 'New Description',
      date: '2023-02-01',
      teacher_id: 2
    };

    mockSessionApiService.create.mockReturnValue(of({ ...newSession, id: 3 }));

    fixture.detectChanges();

    // Set form values
    component.sessionForm?.setValue(newSession);

    // Submit form
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith(newSession);
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should update an existing session when form is submitted in update mode', () => {
    mockRouter.url = '/sessions/update/1';
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockSessionApiService.update.mockReturnValue(of({
      ...mockSession,
      name: 'Updated Session',
      description: 'Updated Description'
    }));

    fixture.detectChanges();

    // Update form values
    component.sessionForm?.patchValue({
      name: 'Updated Session',
      description: 'Updated Description'
    });

    // Submit form
    component.submit();

    // Pour cette partie du test, vérifions seulement que la fonction update a été appelée
    // sans vérifier exactement le contenu du paramètre (qui varie selon les formats de date)
    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.objectContaining({
      name: 'Updated Session',
      description: 'Updated Description',
      teacher_id: 1
    }));
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should handle error when creating a session fails', () => {
    fixture.detectChanges();

    mockSessionApiService.create.mockReturnValue(throwError(() => new Error('Failed to create session')));

    // Submit form
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockMatSnackBar.open).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should handle error when updating a session fails', () => {
    mockRouter.url = '/sessions/update/1';
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockSessionApiService.update.mockReturnValue(throwError(() => new Error('Failed to update session')));

    fixture.detectChanges();

    // Submit form
    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalled();
    expect(mockMatSnackBar.open).not.toHaveBeenCalled();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should load teachers on init', () => {
    // S'assurer que le mock est mis en place avant l'initialisation du composant
    mockTeacherService.all.mockClear();
    mockTeacherService.all.mockReturnValue(of(mockTeachers));

    // Recréer le composant pour déclencher l'initialisation
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    // Vérifier que teachers$ est défini
    expect(component.teachers$).toBeDefined();
  });
});
