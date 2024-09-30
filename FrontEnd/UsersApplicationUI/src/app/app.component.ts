import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from './service/user.service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Users Application UI';

  constructor(private router: Router, public userService: UserService) { }

  onListUsersClick(): void {
    this.userService.isLoggedIn().subscribe(loggedIn => {
      if (!loggedIn) {
        this.router.navigate(['/login']); // Redireciona para a página de login
      } else {
        this.router.navigate(['/users']); // Acessa a página de listagem de usuários
      }
    });
  }

}
