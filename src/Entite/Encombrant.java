package Entite;

import Architecture.Arc;
import Architecture.Rue;

public class Encombrant {
    private Rue rue;
    private Arc localisation;
    private int numero_domicile;

    public  Encombrant(Arc localisation) {
        this.localisation = localisation;
    }
    public Encombrant(Rue rue, int num_domicile) {
        this.rue = rue;
        this.numero_domicile = num_domicile;
        if (num_domicile < rue.getEnsemble_rue().get(0).getNb_habitations()){
            this.localisation = rue.getEnsemble_rue().get(0);
        }
        int temp=0;
        for(int i=0; temp+rue.getEnsemble_rue().get(i).getNb_habitations()<num_domicile;i++){
            if (temp+rue.getEnsemble_rue().get(i).getNb_habitations()<num_domicile){
                this.localisation = rue.getEnsemble_rue().get(i);
                break;
            }else{
                temp+=rue.getEnsemble_rue().get(i).getNb_habitations();
            }
        }
    }

    public Rue getRue() {return rue;}
    public int getNumero_domicile() {return numero_domicile;}
    public Arc getLocalisation() {return localisation;}

    public Encombrant demande(Rue rue, int num_domicile){
        return  new Encombrant(rue,num_domicile);
    }
    public Encombrant demande(Arc localisation){
        return new Encombrant(localisation);
    }
}
