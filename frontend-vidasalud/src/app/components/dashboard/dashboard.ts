import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  @Input() userRole: string = 'Paciente (Cliente)';
  @Output() navigate = new EventEmitter<string>();

  // Mock data for dashboard operations
  kpis = {
    atencionesHoy: 42,
    tiempoEsperaPromedio: '14 min',
    boxesActivos: '18 / 20',
    atencionesPendientes: 7
  };

  waitlist = [
    { ticket: 'T-101', paciente: 'Carolina Mendoza', box: 'Box 03 (Dental)', estado: 'EN_ESPERA', hora: '18:15' },
    { ticket: 'T-102', paciente: 'Gonzalo Sepúlveda', box: 'Box 01 (Med. General)', estado: 'EN_ESPERA', hora: '18:22' },
    { ticket: 'T-103', paciente: 'María José Ríos', box: 'Box 05 (Pediatría)', estado: 'CONFIRMADA', hora: '18:30' }
  ];

  nextAppointments = [
    { fecha: 'Hoy - 19:00 hrs', doctor: 'Dr. Roberto Arancibia', centro: 'Centro Atención Primaria Maipú', especialidad: 'Medicina General', estado: 'CONFIRMADA' },
    { fecha: 'Mañana - 10:30 hrs', doctor: 'Dra. Valentina Castro', centro: 'Clínica Dental Providencia', especialidad: 'Odontología Preventiva', estado: 'SOLICITADA' }
  ];

  onNavigate(tab: string) {
    this.navigate.emit(tab);
  }
}
