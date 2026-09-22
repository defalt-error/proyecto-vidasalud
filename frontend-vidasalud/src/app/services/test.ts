import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MsalService } from '@azure/msal-angular';
import { InteractionRequiredAuthError } from '@azure/msal-browser';
import { Observable, from, switchMap, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TestService {
  private apiUrl = `${environment.apiGatewayUrl}/api/test`;
  private publicApiUrl = `${environment.apiGatewayUrl}/api/public/test`;

  constructor(
    private http: HttpClient,
    private msalService: MsalService
  ) {}

  getPublicTestData(): Observable<any> {
    return this.http.get(this.publicApiUrl);
  }

  getTestData(): Observable<any> {
    const account = this.msalService.instance.getActiveAccount() 
      || this.msalService.instance.getAllAccounts()[0];

    if (!account) {
      return throwError(() => new Error('No hay ninguna cuenta activa. Por favor inicia sesión primero.'));
    }

    const tokenRequest = {
      scopes: environment.azure.scopes,
      account: account
    };

    return from(this.msalService.instance.acquireTokenSilent(tokenRequest)).pipe(
      catchError((error) => {
        if (error instanceof InteractionRequiredAuthError) {
          return from(this.msalService.instance.acquireTokenPopup(tokenRequest));
        }
        return throwError(() => error);
      }),
      switchMap((response) => {
        const headers = new HttpHeaders({
          'Authorization': `Bearer ${response.accessToken}`
        });
        return this.http.get(this.apiUrl, { headers });
      })
    );
  }
}