/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.Persona;
import domain.Usuario;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author rperez
 */
public class JPQLTest {
    static Logger log = LogManager.getRootLogger();
    public static void main(String[] args) {
        String jpql = null;
        Query query = null;
        List<Persona> personas;
        Persona persona;
        Iterator iterator;
        Object[] tupla = null;
        List<String> nombres = null;
        List<Usuario> usuarios;
        
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPQLPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("--------------RESULT------------------");
        selectTuplaPersonaById(entityManager, 1);
    }

    private static void selectTuplaPersonaById(EntityManager entityManager, int id) {
        String jpql;
        Query query;
        Object[] tupla;
        try{
            jpql = "SELECT p.nombre, p.apellido, p.email FROM Persona p WHERE p.idPersona = :idPersona";
        query = entityManager.createQuery(jpql);
        query.setParameter("idPersona", id);
        tupla = (Object[]) query.getSingleResult();
        System.out.println(Arrays.toString(tupla));
        }catch(PersistenceException ex){
            
        }
    }

    private static void selectPersonaById(EntityManager entityManager, int id) {
        String jpql;
        Query query;
        Persona persona;
        jpql = "SELECT p FROM Persona p WHERE p.idPersona = :idPersona";
        query = entityManager.createQuery(jpql);
        query.setParameter("idPersona", id);
        persona = (Persona) query.getSingleResult();
        System.out.println(persona);
    }

    private static void selectAllPersonas(EntityManager entityManager) {
        String jpql;
        Query query;
        List<Persona> personas;
        Iterator iterator;
        jpql = "SELECT p FROM Persona p ";
        query = entityManager.createQuery(jpql);
        personas = query.getResultList();
        iterator = personas.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
