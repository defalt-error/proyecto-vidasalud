import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MsalService } from '@azure/msal-angular';
import { InteractionRequiredAuthError } from '@azure/msal-browser';
import { Observable, from, map, switchMap, catchError, of } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AppointmentDto {
  id?: number | string;
  patientId: string;
  patientName: string;
  patientRut?: string;
  serviceId: string;
  serviceName: string;
  centerId?: string;
  boxId?: string;
  appointmentDate: string;
  status: 'SOLICITADA' | 'CONFIRMADA' | 'EN_ESPERA' | 'EN_ATENCIÓN' | 'CERRADA' | 'CANCELADA';
  reason?: string;
  notes?: string;
  createdBy?: string;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AppointmentsService {
  private apiUrl = `${environment.apiGatewayUrl}/api/appointments`;

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

  getAppointments(status?: string, patientId?: string): Observable<AppointmentDto[]> {
    let params: any = {};
    if (status) params.status = status;
    if (patientId) params.patientId = patientId;

    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.get<AppointmentDto[]>(this.apiUrl, { headers, params })),
      catchError((error) => {
        console.warn('No se pudo conectar con el BFF/Microservicio de Atenciones, usando datos de respaldo:', error);
        return of([]);
      })
    );
  }

  getAppointmentById(id: number | string): Observable<AppointmentDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.get<AppointmentDto>(`${this.apiUrl}/${id}`, { headers }))
    );
  }

  createAppointment(dto: Partial<AppointmentDto>): Observable<AppointmentDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.post<AppointmentDto>(this.apiUrl, dto, { headers }))
    );
  }

  updateStatus(id: number | string, status: string, boxId?: string, notes?: string): Observable<AppointmentDto> {
    return this.getAuthHeaders().pipe(
      switchMap(headers => this.http.put<AppointmentDto>(`${this.apiUrl}/${id}/status`, {
        status,
        boxId,
        notes
      }, { headers }))
    );
  }
}