package application.service.DAO;

import java.util.List;

import application.models.Mcc;

public interface MccDAO {
    void saveMcc(Mcc mcc);
    Mcc getMccById(Long id);
    List<Mcc> getAllMccs();
    void updateMcc(Mcc mcc);
    void deleteMcc(Long id);
    Mcc getMccByCode(String code);
}
