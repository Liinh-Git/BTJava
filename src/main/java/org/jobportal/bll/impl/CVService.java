package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.ICVService;
import org.jobportal.dal.impl.CVDAO;
import org.jobportal.dal.interfaces.ICVDAO;
import org.jobportal.dto.CVDTO;
import org.jobportal.model.CV;
import org.jobportal.model.Education;
import org.jobportal.utils.SessionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CVService - Xu ly nghiep vu quan ly CV cua ung vien.
 * Khong viet SQL, khong goi Swing.
 */
public class CVService implements ICVService {

    private final ICVDAO         cvDAO   = new CVDAO();
    private final SessionManager session = SessionManager.getInstance();

    // ------------------------------------------------------------------
    // ID generation
    // ------------------------------------------------------------------

    /** Sinh cvId format "CV-" + 7 so => 10 ky tu */
    private String generateCvId() {
        long ts = System.currentTimeMillis() % 10_000_000L;
        return String.format("CV-%07d", ts);
    }

    // ------------------------------------------------------------------
    // getCV
    // ------------------------------------------------------------------

    /**
     * Lay CV theo candidateId.
     * Neu chua co CV, tra ve CVDTO rong (khong null) de View hien thi form trong.
     */
    @Override
    public CVDTO getCV(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return new CVDTO();

        CV cv = cvDAO.findByCandidateId(candidateId);
        if (cv == null) {
            // Tra ve DTO rong, View co the dung de hien thi form tao CV moi
            CVDTO empty = new CVDTO();
            empty.setCandidateId(candidateId);
            empty.setEducations(new ArrayList<>());
            return empty;
        }
        return mapToDTO(cv);
    }

    // ------------------------------------------------------------------
    // saveCV
    // ------------------------------------------------------------------

    /**
     * Luu CV: neu chua co thi insert, neu da co thi update.
     * Lay candidateId tu session.
     */
    @Override
    public boolean saveCV(CVDTO cvDTO) {
        if (cvDTO == null) return false;

        String candidateId = session.getCandidateId();
        if (candidateId == null) {
            System.err.println("[CVService] saveCV: chua dang nhap hoac khong phai CANDIDATE.");
            return false;
        }

        // Dat candidateId tu session (bo qua DTO truyen vao de bao mat)
        cvDTO.setCandidateId(candidateId);

        CV existingCV = cvDAO.findByCandidateId(candidateId);

        if (existingCV == null) {
            // Chua co CV => Insert
            String cvId = generateCvId();
            // Kiem tra trung ID (don gian)
            // CV la 1-1 voi candidate nen it khi trung, nhung van nen check
            CV cv = new CV(
                    cvId, candidateId,
                    cvDTO.getObjective(),
                    cvDTO.getSkills(),
                    cvDTO.getDesiredPosition(),
                    cvDTO.getDesiredSalary(),
                    LocalDateTime.now()
            );
            cv.setLocation(cvDTO.getLocation());
            boolean inserted = cvDAO.insert(cv);
            if (inserted) {
                cvDTO.setCvId(cvId);
                return cvDAO.replaceEducations(cvId, cvDTO.getEducations());
            }
            return inserted;
        } else {
            // Da co CV => Update
            existingCV.setObjective(cvDTO.getObjective());
            existingCV.setSkills(cvDTO.getSkills());
            existingCV.setDesiredPosition(cvDTO.getDesiredPosition());
            existingCV.setLocation(cvDTO.getLocation());
            existingCV.setDesiredSalary(cvDTO.getDesiredSalary());
            existingCV.setLastUpdated(LocalDateTime.now());
            cvDTO.setCvId(existingCV.getCvId());
            boolean updated = cvDAO.update(existingCV);
            if (updated) {
                return cvDAO.replaceEducations(existingCV.getCvId(), cvDTO.getEducations());
            }
            return false;
        }
    }

    // ------------------------------------------------------------------
    // Private helper
    // ------------------------------------------------------------------

    private CVDTO mapToDTO(CV cv) {
        CVDTO dto = new CVDTO();
        dto.setCvId(cv.getCvId());
        dto.setCandidateId(cv.getCandidateId());
        dto.setObjective(cv.getObjective());
        dto.setSkills(cv.getSkills());
        dto.setDesiredPosition(cv.getDesiredPosition());
        dto.setLocation(cv.getLocation());
        dto.setDesiredSalary(cv.getDesiredSalary());
        dto.setLastUpdated(cv.getLastUpdated());
        List<Education> educations = cvDAO.findEducationsByCvId(cv.getCvId());
        dto.setEducations(educations != null ? educations : new ArrayList<>());
        return dto;
    }
}
