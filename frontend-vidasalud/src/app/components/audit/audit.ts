import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface AuditLog {
  id: string;
  timestamp: string;
  usuario: string;
  rol: string;
  evento: string;
  atencionId: string;
  origen: string;
}

@Component({
  selector: 'app-audit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './audit.html',
  styleUrl: './audit.css'
})
export class AuditComponent {
  searchTerm: string = '';

  logs: AuditLog[] = [
    { id: 'LOG-8801', timestamp: '2026-09-13 18:22:10', usuario: 'recepcion.maipu@vidasalud.cl', rol: 'Receponista', evento: 'LLAMADO_A_BOX', atencionId: '#1', origen: 'BFF Gateway' },
    { id: 'LOG-8802', timestamp: '2026-09-13 18:15:04', usuario: 'carolina.mendoza@gmail.com', rol: 'Paciente', evento: 'CREACION_ATENCION', atencionId: '#1', origen: 'Web Portal' },
    { id: 'LOG-8803', timestamp: '2026-09-13 18:00:45', usuario: 'admin.general@vidasalud.cl', rol: 'Admin', evento: 'ACTUALIZACION_ARANCEL', atencionId: 'N/A', origen: 'BFF Gateway' },
    { id: 'LOG-8804', timestamp: '2026-09-13 17:45:12', usuario: 'dr.arancibia@vidasalud.cl', rol: 'Médico', evento: 'CIERRE_ATENCION', atencionId: '#4', origen: 'Box Terminal' }
  ];

  get filteredLogs(): AuditLog[] {
    if (!this.searchTerm) return this.logs;
    const term = this.searchTerm.toLowerCase();
    return this.logs.filter(l => 
      l.usuario.toLowerCase().includes(term) || 
      l.evento.toLowerCase().includes(term) || 
      l.atencionId.toLowerCase().includes(term)
    );
  }
}
