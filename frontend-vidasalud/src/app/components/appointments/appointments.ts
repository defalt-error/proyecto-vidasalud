import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentsService, AppointmentDto } from '../../services/appointments.service';

export interface Appointment {
  id: string;
  paciente: string;
  rut: string;
  prestacion: string;
  box: string;
  fecha: string;
  status: 'SOLICITADA' | 'CONFIRMADA' | 'EN_ESPERA' | 'EN_ATENCIÓN' | 'CERRADA' | 'CANCELADA';
}

@Component({
  selector: 'app-appointments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointments.html',
  styleUrl: './appointments.css'
})
export class AppointmentsComponent implements OnInit {
  @Input() userRole: string = 'Paciente (Cliente)';

  showForm: boolean = false;
  loading: boolean = false;

  newAppointment = {
    paciente: '',
    rut: '',
    prestacion: 'Medicina General',
    box: 'Box 01 Med. General',
    fecha: ''
  };

  appointments: Appointment[] = [
    { id: '1', paciente: 'Carolina Mendoza', rut: '15.432.189-0', prestacion: 'Limpieza Dental', box: 'Box 03 Dental', fecha: '2026-09-13 18:15', status: 'EN_ESPERA' },
    { id: '2', paciente: 'Gonzalo Sepúlveda', rut: '18.765.432-1', prestacion: 'Consulta Médica General', box: 'Box 01 Med. General', fecha: '2026-09-13 18:30', status: 'SOLICITADA' },
    { id: '3', paciente: 'María José Ríos', rut: '12.987.654-K', prestacion: 'Pediatría Preventiva', box: 'Box 05 Pediatría', fecha: '2026-09-13 19:00', status: 'CONFIRMADA' },
    { id: '4', paciente: 'Carlos Alarcón', rut: '16.543.210-9', prestacion: 'Chequeo Dental', box: 'Box 03 Dental', fecha: '2026-09-13 17:00', status: 'CERRADA' }
  ];

  prestaciones = ['Consulta Médica General', 'Limpieza Dental', 'Pediatría Preventiva', 'Examen Cardiológico', 'Radiografía Panorámica'];
  boxes = ['Box 01 Med. General', 'Box 02 Med. General', 'Box 03 Dental', 'Box 04 Dental', 'Box 05 Pediatría'];

  constructor(
    private appointmentsService: AppointmentsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.loading = true;
    this.appointmentsService.getAppointments().subscribe({
      next: (dtos: AppointmentDto[]) => {
        this.loading = false;
        if (dtos && dtos.length > 0) {
          this.appointments = dtos.map(dto => ({
            id: dto.id ? dto.id.toString() : '0',
            paciente: dto.patientName,
            rut: dto.patientRut || 'Sin RUT',
            prestacion: dto.serviceName,
            box: dto.boxId || 'Sin asignar',
            fecha: dto.appointmentDate ? dto.appointmentDate.replace('T', ' ').substring(0, 16) : '',
            status: dto.status
          }));
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.loading = false;
        console.warn('Usando datos locales por falla en backend:', err);
        this.cdr.detectChanges();
      }
    });
  }

  toggleForm() {
    this.showForm = !this.showForm;
    this.cdr.detectChanges();
  }

  createAppointment() {
    if (!this.newAppointment.paciente || !this.newAppointment.rut) return;

    const dto: Partial<AppointmentDto> = {
      patientId: 'PAC-' + Math.floor(100 + Math.random() * 900),
      patientName: this.newAppointment.paciente,
      patientRut: this.newAppointment.rut,
      serviceId: 'SRV-01',
      serviceName: this.newAppointment.prestacion,
      boxId: this.newAppointment.box,
      appointmentDate: new Date().toISOString(),
      reason: 'Solicitud desde Portal VidaSalud'
    };

    this.appointmentsService.createAppointment(dto).subscribe({
      next: (created) => {
        this.loadAppointments();
        this.newAppointment = { paciente: '', rut: '', prestacion: 'Medicina General', box: 'Box 01 Med. General', fecha: '' };
        this.showForm = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        // Fallback local
        const localCreated: Appointment = {
          id: (this.appointments.length + 1).toString(),
          paciente: this.newAppointment.paciente,
          rut: this.newAppointment.rut,
          prestacion: this.newAppointment.prestacion,
          box: this.newAppointment.box,
          fecha: new Date().toLocaleString(),
          status: 'SOLICITADA'
        };
        this.appointments.unshift(localCreated);
        this.newAppointment = { paciente: '', rut: '', prestacion: 'Medicina General', box: 'Box 01 Med. General', fecha: '' };
        this.showForm = false;
        this.cdr.detectChanges();
      }
    });
  }

  changeStatus(app: Appointment, newStatus: Appointment['status']) {
    // Regla de Negocio VidaSalud: No se puede pasar a EN_ATENCIÓN sin estar previamente CONFIRMADA
    if (newStatus === 'EN_ATENCIÓN' && app.status === 'SOLICITADA') {
      alert('Regla de Negocio: No se puede pasar una atención a EN_ATENCIÓN sin haber sido CONFIRMADA previamente.');
      return;
    }

    // Role enforcement
    if (['CONFIRMADA', 'EN_ESPERA', 'EN_ATENCIÓN', 'CERRADA'].includes(newStatus) && !this.canManageFlow()) {
      alert(`Acceso Denegado: El rol "${this.userRole}" no tiene permisos para cambiar el estado a ${newStatus}. Esta acción corresponde a la Recepcionista (Operador) o Admin.`);
      return;
    }

    this.appointmentsService.updateStatus(app.id, newStatus).subscribe({
      next: () => {
        app.status = newStatus;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('Actualizando estado localmente tras respuesta:', err);
        app.status = newStatus;
        this.cdr.detectChanges();
      }
    });
  }

  canCreate(): boolean {
    return !this.userRole.includes('Auditor');
  }

  canManageFlow(): boolean {
    return this.userRole.includes('Admin') || this.userRole.includes('Recepcionista') || this.userRole.includes('Operador');
  }

  canCancel(): boolean {
    return !this.userRole.includes('Auditor');
  }
}