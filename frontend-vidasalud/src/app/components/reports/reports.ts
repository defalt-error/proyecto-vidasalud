import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css'
})
export class ReportsComponent {
  range: string = 'last24h';

  topServices = [
    { nombre: 'Consulta Médica General', atenciones: 142, porcentaje: '45%' },
    { nombre: 'Limpieza Dental e Higiene', atenciones: 88, porcentaje: '28%' },
    { nombre: 'Pediatría Preventiva', atenciones: 54, porcentaje: '17%' },
    { nombre: 'Radiografía Panorámica', atenciones: 32, porcentaje: '10%' }
  ];

  hourlyData = [
    { hora: '08:00', atenciones: 12 },
    { hora: '10:00', atenciones: 28 },
    { hora: '12:00', atenciones: 35 },
    { hora: '14:00', atenciones: 19 },
    { hora: '16:00', atenciones: 42 },
    { hora: '18:00', atenciones: 31 }
  ];
}
