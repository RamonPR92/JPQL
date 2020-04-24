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
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private String jpql;
    private Query query;
    private List<Persona> personas;
    private Iterator iterator;
    private Object[] tupla;
    private Persona persona;

    /**
     * @return the entityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * @param entityManagerFactory the entityManagerFactory to set
     */
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void main(String[] args) {
        JPQLTest jpqlt = new JPQLTest();
        jpqlt.setEntityManagerFactory(Persistence.createEntityManagerFactory("JPQLPU"));
        jpqlt.setEntityManager(jpqlt.getEntityManagerFactory().createEntityManager());

        System.out.println("--------------RESULT------------------");
        jpqlt.selectAllIdAsPersonaObject();
        jpqlt.selectMaxMinCountId();
        jpqlt.getPersonaByMatchLetter("%a%");
    }

       /**
     * Selecciona Solo ciertos atributos de una persona por id, pero estos son 
     * regresados como arreglos tipo Object
     */
    public void selectTuplaPersonaById(int id) {

        try {
            jpql = "SELECT p.nombre, p.apellido, p.email FROM persona p WHERE p.idPersona = :idPersona";
            query = entityManager.createQuery(jpql);
            query.setParameter("idPersona", id);
            tupla = (Object[]) query.getSingleResult();
            System.out.println(Arrays.toString(tupla));
        } catch (PersistenceException ex) {
            log.error("Error al intentar recuperar la tupla", ex);
        }
    }

    /**
     * Selecciona Solo ciertos atributos de todas las personas, pero estos son 
     * regresados como arreglos tipo Object
     */
    public void selectAllTuplas() {
        jpql = "SELECT p.nombre, p.apellido, p.email FROM Persona p ";
        query = entityManager.createQuery(jpql);
        iterator = query.getResultList().iterator();
        while(iterator.hasNext()){
            tupla = (Object [])iterator.next();
            String nombre = (String)tupla[0];
            String apellido = (String)tupla[1];
            String email = (String)tupla[2];
            System.out.println(nombre +", " + apellido + ", "  + email);
        }
    }

    /**
     * Selecciona a una persona por medio de el id
     * @param id 
     */
    public void selectPersonaById(int id) {
        jpql = "SELECT p FROM Persona p WHERE p.idPersona = :idPersona";
        query = entityManager.createQuery(jpql);
        query.setParameter("idPersona", id);
        persona = (Persona) query.getSingleResult();
        System.out.println(persona);
    }

    /**
     * Selecciona todas las personas 
     */
    public void selectAllPersonas() {

        jpql = "SELECT p FROM Persona p ";
        query = entityManager.createQuery(jpql);
        personas = query.getResultList();
        iterator = personas.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
    /**
     * Obtiene todos los objetos persona pero solo con el id y no con todos los atributos, 
     * es importante contar con un constructor que se adapte a la consulta
     */
    public void selectAllIdAsPersonaObject(){
        jpql = "SELECT new domain.Persona(p.idPersona) FROM Persona p";
        personas = entityManager.createQuery(jpql).getResultList();
        personas.forEach(System.out::println);
    }
    
    /**
     * Selecciona el valor minimo, el valor maximo del id en la tabla, y tambien el total de ids,
     * todos estos valores son entregados como un Object[]
     */
    public void selectMaxMinCountId(){
        jpql = "SELECT max(p.idPersona) as Maximo, min(p.idPersona) as Minimo, count(p.idPersona) as Contador FROM Persona p";
        iterator = entityManager.createQuery(jpql).getResultList().iterator();
        while(iterator.hasNext()){
            tupla = (Object[]) iterator.next();
            Integer max = (Integer) tupla[0];
            Integer min = (Integer) tupla[1];
            Long count = (Long) tupla[2];
            System.out.println("Min " + min + " Max " + max + " Count " + count);
        }
    }
    
    /**
     * Obtiene el listado de personas segun la coincidencia que enviemos como parametros    
     * @param patern 
     */
    public void getPersonaByMatchLetter(String patern){
        jpql = "SELECT p FROM Persona p WHERE upper(p.nombre) like upper(:parametro)";
        query = entityManager.createQuery(jpql);
        query.setParameter("parametro", patern);
        personas = query.getResultList();
        personas.forEach(System.out::println);
    }
}
