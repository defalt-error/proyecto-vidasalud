import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent {
  @Input() activeTab: string = 'dashboard';
  @Input() activeAccount: any = null;
  @Input() userRole: string = 'Paciente (Cliente)';

  @Output() tabChange = new EventEmitter<string>();
  @Output() roleChange = new EventEmitter<string>();
  @Output() logoutEvent = new EventEmitter<void>();

  availableRoles: string[] = [
    'Admin',
    'Recepcionista (Operador)',
    'Paciente (Cliente)',
    'Auditor'
  ];

  selectTab(tab: string): void {
    if (this.canViewTab(tab)) {
      this.tabChange.emit(tab);
    }
  }

  onRoleSelect(newRole: string): void {
    this.roleChange.emit(newRole);
  }

  logout(): void {
    this.logoutEvent.emit();
  }

  canViewTab(tab: string): boolean {
    const role = this.userRole;
    if (role.includes('Admin')) return true;

    if (role.includes('Recepcionista') || role.includes('Operador')) {
      return ['dashboard', 'appointments', 'catalog'].includes(tab);
    }

    if (role.includes('Paciente') || role.includes('Cliente')) {
      return ['dashboard', 'appointments'].includes(tab);
    }

    if (role.includes('Auditor')) {
      return ['dashboard', 'audit'].includes(tab);
    }

    return tab === 'dashboard' || tab === 'appointments';
  }
}
