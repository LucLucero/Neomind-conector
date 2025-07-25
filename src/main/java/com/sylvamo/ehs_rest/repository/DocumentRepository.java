package com.sylvamo.ehs_rest.repository;

import com.sylvamo.ehs_rest.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    @Query(value =
            "SELECT DATAUPDATE, NEOID, CODE, NOMEDOCUMENTO, NOME, RN " +
                    "FROM V_DOCS_NEOMIND "
                    , nativeQuery = true)
    List<Document> getAllRegisteredDocuments();

    @Query(value =
            "SELECT DATAUPDATE, NEOID, CODE, NOMEDOCUMENTO, NOME, RN " +
                    " FROM V_DOCS_NEOMIND " +
                    " WHERE DATAUPDATE >= TRUNC(SYSDATE)", nativeQuery = true)
    List<Document> getUpdated();
}

