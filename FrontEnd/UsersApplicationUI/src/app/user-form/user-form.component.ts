import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../service/user.service.service';
import { User } from '../model/user';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {

  user: User;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {
    this.user = new User();
  }

  onSubmit(): void {
    this.userService.register(this.user).subscribe(
      response => {
        this.successMessage = 'Usuário registrado com sucesso!';
        this.errorMessage = '';

        setTimeout(() => {
          this.successMessage = '';
        }, 5000);
      },
      error => {
        this.errorMessage = error.error || 'Erro ao registrar usuário'; // Captura a mensagem de erro do backend
        console.error('Erro ao registrar usuário', error);
      }
    );
  }

  gotoUserList() {
    this.router.navigate(['/users']);
  }

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