/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author RamonPR92
 */
public class PruebaApiCriteria {

    static Logger log = LogManager.getRootLogger();
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Persona> criteriaQuery;
    private Root<Persona> root;
    private TypedQuery<Persona> typedQuery;


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
    
    public void selectAllPersonas(){
        //Se crea el criteria apartir de el entityManager
        criteriaBuilder = entityManager.getCriteriaBuilder();
        //Se crea un criteria Query apartir de un criteria builder pero pasamos la clase con la que vamos a trabajar
        criteriaQuery = criteriaBuilder.createQuery(Persona.class);
        //Se crea una raiz aprtir de un criteria query, 
        //que es una parte de la consulta, la parte del filtro, indicamos la clase con la que se va a trabajar
        root = criteriaQuery.from(Persona.class);
        //Asignamos la raiz a la operacion select del criteria query
        criteriaQuery.select(root);
        
        //creamos el typed query por medio del entity manager pasando la estructura del criteria query
        typedQuery = entityManager.createQuery(criteriaQuery);
        //El typed query nos da el resultado 
        List<Persona> personas = typedQuery.getResultList();
        log.debug("--- Resultado de lista de personas ---");
        personas.forEach(System.out::println);
    }
    
    public void selectPersonaById(int id){
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Persona.class);
        root = criteriaQuery.from(Persona.class);
        //Cuando solo hay un criterio
        //criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("idPersona"), id));
        
        //Cuando agregamos mas de uno
        criteriaQuery.select(root);
        List<Predicate> criterios = new ArrayList<>();
        ParameterExpression<Integer> parameter = criteriaBuilder.parameter(Integer.class, "idPersona");
        criterios.add(criteriaBuilder.equal(root.get("idPersona"), parameter));
        if(criterios.size() == 1){
            criteriaQuery.where(criterios.get(0));
        }else if(criterios.size() > 1){
            criteriaQuery.where(criteriaBuilder.and(criterios.toArray(new Predicate[0])));
        }//fin de proceso que agrega todos los criterios
        
        typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setParameter("idPersona", id);
        Persona persona = typedQuery.getSingleResult();
        log.debug("--- Resultado de personacon id " + id + " ---");
        System.out.println(persona);
    }

    public static void main(String[] args) {
        PruebaApiCriteria pruebaApiCriteria = new PruebaApiCriteria();
        pruebaApiCriteria.setEntityManagerFactory(Persistence.createEntityManagerFactory("JPQLPU"));
        pruebaApiCriteria.setEntityManager(pruebaApiCriteria.getEntityManagerFactory().createEntityManager());  
        pruebaApiCriteria.selectAllPersonas();
        pruebaApiCriteria.selectPersonaById(5);
    }

}
