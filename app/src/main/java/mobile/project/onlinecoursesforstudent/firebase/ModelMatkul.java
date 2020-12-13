package mobile.project.onlinecoursesforstudent.firebase;

public class ModelMatkul {
    String Matkul, Nama, email, statusPelajaran;

    public String getMatkul() {
        return Matkul;
    }

    public void setMatkul(String matkul) {
        Matkul = matkul;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusPelajaran() {
        return statusPelajaran;
    }

    public void setStatusPelajaran(String statusPelajaran) {
        this.statusPelajaran = statusPelajaran;
    }
}
