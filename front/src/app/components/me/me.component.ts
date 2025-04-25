import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user.interface';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit {

  public user: User | undefined;

  constructor(private router: Router,
              private sessionService: SessionService,
              private matSnackBar: MatSnackBar,
              private userService: UserService) {
  }

  public ngOnInit(): void {
    this.userService
      .getById(this.sessionService.sessionInformation!.id.toString())
      .subscribe({
        next: (user: User) => this.user = user,
        error: (_) => this.matSnackBar.open('Error loading user', 'Close', { duration: 3000 })
      });
  }

  public back(): void {
    window.history.back();
  }

  public delete(): void {
    if (window.confirm('Are you sure you want to delete your account?')) {
      this.userService
        .delete(this.sessionService.sessionInformation!.id.toString())
        .subscribe({
          next: (_) => {
            this.matSnackBar.open("Your account has been deleted !", 'Close', { duration: 3000 });
            this.sessionService.logOut();
            this.router.navigate(['/']);
          },
          error: (_) => this.matSnackBar.open('Error deleting account', 'Close', { duration: 3000 })
        });
    }
  }
}
