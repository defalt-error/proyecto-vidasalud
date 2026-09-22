package cl.duoc.vidasalud.appointments.model;

public enum AppointmentStatus {
    SOLICITADA,
    CONFIRMADA,
    EN_ESPERA,
    EN_ATENCIÓN,
    CERRADA,
    CANCELADA;

    public static boolean isValid(String status) {
        if (status == null) return false;
        try {
            valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
