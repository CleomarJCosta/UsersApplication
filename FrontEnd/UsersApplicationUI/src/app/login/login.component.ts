import { Component } from '@angular/core';

import { Router } from '@angular/router';
import { UserService } from '../service/user.service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) { }

  onSubmit() {
    this.userService.login(this.email, this.password).subscribe(
      (response) => {
        console.log('Login bem-sucedido:', response); // Opcional, mas útil para depuração
        this.userService.setLoggedIn(true);  // Atualiza o estado de login
        this.router.navigate(['/users']);  // Redireciona para a lista de usuários ao logar com sucesso
      },
      (error) => {
        console.error('Erro no login:', error);
        this.errorMessage = 'Login falhou. Verifique suas credenciais.';
      }
    );
  }

  // Método para sair
  onLogout() {
    this.userService.logout();  // Chama o método de logout
    this.router.navigate(['/login']);  // Redireciona para a página de login
  }

}