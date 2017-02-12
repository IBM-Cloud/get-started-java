/*
 * Copyright IBM Corp. 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wasdev.sample.store;

import java.util.Collection;

import com.cloudant.client.api.Database;

import wasdev.sample.Visitor;

/**
 * Defines the API for a ToDo store.
 *
 */
public interface VisitorStore {

  	/**
	 * Get the target db object.
	 * 
	 * @return Database.
  	 * @throws Exception 
	 */
  public Database getDB();
  
  	/**
	 * Gets all Visitors from the store.
	 * 
	 * @return All Visitors.
  	 * @throws Exception 
	 */
  public Collection<Visitor> getAll();

  /**
   * Gets an individual ToDo from the store.
   * @param id The ID of the ToDo to get.
   * @return The ToDo.
   */
  public Visitor get(String id);

  /**
   * Persists a Visitor to the store.
   * @param td The ToDo to persist.
   * @return The persisted ToDo.  The ToDo will not have a unique ID..
   */
  public Visitor persist(Visitor vi);

  /**
   * Updates a ToDo in the store.
   * @param id The ID of the Visitor to update.
   * @param td The Visitor with updated information.
   * @return The updated Visitor.
   */
  public Visitor update(String id, Visitor vi);

  /**
   * Deletes a ToDo from the store.
   * @param id The ID of the Visitor to delete.
   */
  public void delete(String id);
  
  /**
   * Counts the number of Visitors
   * @return The total number of Visitors.
 * @throws Exception 
   */
  public int count() throws Exception;
}
