import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../model/user';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private usersUrl: string;
  private authUrl: string; // URL para autenticação
  private loggedIn = new BehaviorSubject<boolean>(false); // Estado de login

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/api/users';
    this.authUrl = 'http://localhost:8080/api/users/login';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }


  // Método para registrar um novo usuário
  public register(user: User): Observable<User> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<User>(`${this.usersUrl}/register`, user, { headers });
  }

  // Método para fazer login
  public login(email: string, password: string): Observable<string> {
    const body = new URLSearchParams();
    body.set('email', email);
    body.set('password', password);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    return this.http.post<string>(this.authUrl, body.toString(), { headers });
  }

  // Método para definir o estado de login
  public setLoggedIn(value: boolean) {
    this.loggedIn.next(value);
  }

  // Método para verificar o estado de login
  public isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  public logout(): void {
    this.setLoggedIn(false);  // Atualiza o estado de login para false
  }
}
