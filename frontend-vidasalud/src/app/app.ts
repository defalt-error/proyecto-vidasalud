import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MsalService } from '@azure/msal-angular';
import { TestService } from './services/test';
import { NavbarComponent } from './components/navbar/navbar';
import { DashboardComponent } from './components/dashboard/dashboard';
import { AppointmentsComponent } from './components/appointments/appointments';
import { CatalogComponent } from './components/catalog/catalog';
import { ReportsComponent } from './components/reports/reports';
import { AuditComponent } from './components/audit/audit';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    DashboardComponent,
    AppointmentsComponent,
    CatalogComponent,
    ReportsComponent,
    AuditComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {
  activeTab: string = 'dashboard';
  apiMessage: string = '';
  selectedRoleOverride: string | null = null;

  constructor(
    private msal: MsalService,
    private testService: TestService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.msal.handleRedirectObservable().subscribe({
      next: (result) => {
        if (result) {
          this.msal.instance.setActiveAccount(result.account);
        } else {
          this.setAccountIfAvailable();
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('Error procesando redirect MSAL (limpiando estado desincronizado):', err);
        if (window.location.hash || window.location.search) {
          window.history.replaceState({}, document.title, window.location.pathname);
        }
        this.setAccountIfAvailable();
        this.cdr.detectChanges();
      }
    });
  }

  private setAccountIfAvailable(): void {
    const activeAccount = this.msal.instance.getActiveAccount();
    if (!activeAccount) {
      const accounts = this.msal.instance.getAllAccounts();
      if (accounts.length > 0) {
        this.msal.instance.setActiveAccount(accounts[0]);
      }
    }
  }

  getActiveAccount() {
    return this.msal.instance.getActiveAccount() || this.msal.instance.getAllAccounts()[0] || null;
  }

  getUserRole(): string {
    if (this.selectedRoleOverride) {
      return this.selectedRoleOverride;
    }
    const account = this.getActiveAccount();
    if (!account) return 'Invitado';

    const claims: any = account.idTokenClaims;
    if (claims && claims.roles && claims.roles.length > 0) {
      return claims.roles[0];
    }
    return 'Paciente (Cliente)';
  }

  onRoleChange(newRole: string) {
    this.selectedRoleOverride = newRole;
    if (newRole.includes('Cliente') || newRole.includes('Paciente')) {
      if (['catalog', 'reports', 'audit'].includes(this.activeTab)) {
        this.activeTab = 'dashboard';
      }
    } else if (newRole.includes('Recepcionista') || newRole.includes('Operador')) {
      if (['reports', 'audit'].includes(this.activeTab)) {
        this.activeTab = 'dashboard';
      }
    } else if (newRole.includes('Auditor')) {
      if (['catalog', 'reports'].includes(this.activeTab)) {
        this.activeTab = 'dashboard';
      }
    }
    this.cdr.detectChanges();
  }

  isLoggedIn(): boolean {
    return this.msal.instance.getAllAccounts().length > 0;
  }

  login(): void {
    this.msal.loginRedirect();
  }

  logout(): void {
    this.msal.logoutRedirect();
  }

  onTabChange(tab: string) {
    this.activeTab = tab;
    this.cdr.detectChanges();
  }

  callPublicBackend(): void {
    this.testService.getPublicTestData().subscribe({
      next: (res) => {
        this.apiMessage = res.message || JSON.stringify(res);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al llamar al endpoint público del BFF:', err);
        this.cdr.detectChanges();
      }
    });
  }

  callBackend(): void {
    const accounts = this.msal.instance.getAllAccounts();
    if (accounts.length === 0) {
      console.error('No hay ninguna cuenta autenticada. Haz clic en Iniciar Sesión.');
      return;
    }
    
    this.msal.instance.setActiveAccount(accounts[0]);

    this.testService.getTestData().subscribe({
      next: (res) => {
        this.apiMessage = res.message || JSON.stringify(res);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error al llamar al BFF:', err);
        this.cdr.detectChanges();
      }
    });
  }
}