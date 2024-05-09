package application.service;

import java.util.List;

import application.models.Mcc;
import application.service.DAO.MccDAO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MccService {
    
    private MccDAO mccDAO;

    public void saveMcc(Mcc mcc) {
        mccDAO.saveMcc(mcc);
    }

    public Mcc getMccById(Long id) {
        return mccDAO.getMccById(id);
    }

    public List<Mcc> getAllMccs() {
        return mccDAO.getAllMccs();
    }

    public void updateMcc(Mcc mcc) {
        mccDAO.updateMcc(mcc);
    }

    public void deleteMcc(Long id) {
        mccDAO.deleteMcc(id);
    }

    public Mcc getMccByCode(String code){
        return mccDAO.getMccByCode(code);
    }

    public boolean isMccCodeExists(String code){
        Mcc mcc = mccDAO.getMccByCode(code);
        if(mcc == null){
            return false;
        }
        return true;

    } 
}
