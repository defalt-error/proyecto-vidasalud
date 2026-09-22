import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MsalService } from '@azure/msal-angular';
import { InteractionRequiredAuthError } from '@azure/msal-browser';
import { Observable, from, map, switchMap, catchError, of } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ServiceCatalogDto {
  id: number | string;
  code: string;
  name: string;
  description?: string;
  category: string;
  price: number;
  availableQuota: number;
  active?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface BoxClinicalDto {
  id: number | string;
  code: string;
  name: string;
  centerId?: string;
  specialty?: string;
  active?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class CatalogServiceFrontend {
  private apiUrl = `${environment.apiGatewayUrl}/api/catalog`;
  private directCatalogUrl = 'http://localhost:8083/api/catalog';

  constructor(
    private http: HttpClient,
    private msalService: MsalService
  ) {}

  private getAuthHeaders(): Observable<HttpHeaders> {
    const account = this.msalService.instance.getActiveAccount() 
      || this.msalService.instance.getAllAccounts()[0];

    if (!account) {
      return of(new HttpHeaders());
    }

    const tokenRequest = {
      scopes: environment.azure.scopes,
      account: account
    };

    return from(this.msalService.instance.acquireTokenSilent(tokenRequest)).pipe(
      map(response => new HttpHeaders({ 'Authorization': `Bearer ${response.accessToken}` })),
      catchError(error => {
        if (error instanceof InteractionRequiredAuthError) {
          return from(this.msalService.instance.acquireTokenPopup(tokenRequest)).pipe(
            map(response => new HttpHeaders({ 'Authorization': `Bearer ${response.accessToken}` })),
            catchError(() => of(new HttpHeaders()))
          );
        }
        return of(new HttpHeaders());
      })
    );
  }

  getServices(): Observable<ServiceCatalogDto[]> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.get<ServiceCatalogDto[]>(`${this.apiUrl}/services`, { headers })),
      catchError(() => {
        return this.http.get<ServiceCatalogDto[]>(`${this.directCatalogUrl}/services`).pipe(
          catchError((err) => {
            console.warn('Usando catálogo inicial en memoria:', err);
            return of([
              { id: 1, code: 'MED-GEN-01', name: 'Consulta Medicina General', description: 'Atención médica preventiva', category: 'Medicina General', price: 25000, availableQuota: 15, active: true },
              { id: 2, code: 'DEN-PREV-01', name: 'Atención Dental Preventiva', description: 'Limpieza dental y flúor', category: 'Odontología', price: 45000, availableQuota: 8, active: true },
              { id: 3, code: 'TELE-MED-01', name: 'Telemedicina Urgencia Leve', description: 'Consulta remota', category: 'Telemedicina', price: 18000, availableQuota: 25, active: true },
              { id: 4, code: 'PED-GEN-01', name: 'Consulta Pediatría', description: 'Control de niño sano', category: 'Pediatría', price: 32000, availableQuota: 10, active: true }
            ]);
          })
        );
      })
    );
  }

  createService(dto: Partial<ServiceCatalogDto>): Observable<ServiceCatalogDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.post<ServiceCatalogDto>(`${this.apiUrl}/services`, dto, { headers })),
      catchError(() => this.http.post<ServiceCatalogDto>(`${this.directCatalogUrl}/services`, dto))
    );
  }

  updateService(id: number | string, dto: Partial<ServiceCatalogDto>): Observable<ServiceCatalogDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.put<ServiceCatalogDto>(`${this.apiUrl}/services/${id}`, dto, { headers })),
      catchError(() => this.http.put<ServiceCatalogDto>(`${this.directCatalogUrl}/services/${id}`, dto))
    );
  }

  deleteService(id: number | string): Observable<void> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.delete<void>(`${this.apiUrl}/services/${id}`, { headers })),
      catchError(() => this.http.delete<void>(`${this.directCatalogUrl}/services/${id}`))
    );
  }

  getBoxes(): Observable<BoxClinicalDto[]> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.get<BoxClinicalDto[]>(`${this.apiUrl}/boxes`, { headers })),
      catchError(() => {
        return this.http.get<BoxClinicalDto[]>(`${this.directCatalogUrl}/boxes`).pipe(
          catchError(() => of([
            { id: 1, code: 'BOX-101', name: 'Box 101 - Consulta General', centerId: 'CENTRO-SANTIAGO-CENTRO', specialty: 'Medicina General', active: true },
            { id: 2, code: 'BOX-102', name: 'Box 102 - Pediatría', centerId: 'CENTRO-SANTIAGO-CENTRO', specialty: 'Pediatría', active: true },
            { id: 3, code: 'BOX-DENTAL-01', name: 'Box Dental 01', centerId: 'CENTRO-PROVIDENCIA', specialty: 'Odontología', active: true }
          ]))
        );
      })
    );
  }

  createBox(dto: Partial<BoxClinicalDto>): Observable<BoxClinicalDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.post<BoxClinicalDto>(`${this.apiUrl}/boxes`, dto, { headers })),
      catchError(() => this.http.post<BoxClinicalDto>(`${this.directCatalogUrl}/boxes`, dto))
    );
  }
}
