package fr.qui.gestion.frais;

public enum Frequence {
	MENSUELLE("Mensuelle"),
    TRIMESTRIELLE("Trimestrielle"),
    ANNUELLE("Annuelle"),
    PONCTUELLE("Ponctuelle");

    private String libelle;

    Frequence(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
    
    public double convertirMontantAnnuel(double montant) {
        switch (this) {
            case MENSUELLE:
                return montant * 12.0;
            case TRIMESTRIELLE:
                return montant * 4.0;
            case ANNUELLE:
                return montant;
            case PONCTUELLE:
                return montant;
            default:
                throw new IllegalArgumentException("Fréquence de frais invalide : " + this);
        }
    }
}
