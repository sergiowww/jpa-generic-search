# JPA Generic Search #
Create criteria queries using JPA by just annotating fields that filter the results.
It simplifies filter queries using JPA2 and its Criteria API.

## Example ##
Given that there is an entity class named `Category` with its definition as following:
```
@Entity
@Access(AccessType.PROPERTY)
public class Category implements Serializable {
	private static final long serialVersionUID = 213096796308900276L;

	@FilterParameter(FilterType.EQUALS)
	private Integer id;

	@FilterParameter(FilterType.ILIKE)
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cat_id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "cat_ds", nullable = false, length = 30)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description ) {
		this.description = description ;
	}

}
```

To query by part of description or the identifier number, this entity has been annotated with FilterParameter, that was placed over the fields `description` and `id` respectively.
A field annotated with FilterParameter indicates that its value is going to be used to filter results by using the filter type indicated.
The JPA Generic Search provides the following filter types (FilterType), which are internally created by a `javax.persistence.criteria.CriteriaBuilder`:
  * `ILIKE`: Use the value of the annotated field to create a insensitive like over an entity property
  * `EQUALS`: It performs an equals criteria over an entity property
  * `GREATER_THAN_OR_EQUAL`: Filter items which has a value "greater than or equal" the value provided in the field
  * `LESS_THAN_OR_EQUAL`: Filter items which has a value "less than or equal" the value provided in the field
  * `SCAN_FILTERS_INSIDE_THIS`: This one is not a filter type, but it indicates to the framework that this object contains more filters that should be used.

By default, JPA Generic Search consider fields with non-null values to perform the query.
Its highly recommended that you use Spring Framework to manage dependency injection of your project. By doing so, you also need to add `GenericSearchDAO` class to your applicationContext.xml (or by using spring annotation scanning).

See the following example, which lists all entities that contains the string "Second" on property `description` of the entity Category.

```
@PersistenceContext
private EntityManager entityManager;

@Test
public void testCategory() throws Exception {
	//the objects category1 and category2 are our records to be queried.
	Category category1 = new Category();
	category1.setDescription("First Category");

	Category category2 = new Category();
	category2.setDescription("Second Category");

	entityManager.persist(category1);
	entityManager.persist(category2);

	//At this point, a new category is instantiated and used to store the description value used to filter the results

	Category searchCategory = new Category();
	searchCategory.setDescription("Second");

	//List the results
	List<Category> result = genericSearchDAO.list(Category.class, searchCategory);
	assertEquals(1, result.size());
	Category categoryFound = result.get(0);
	assertEquals("Second Category", categoryFound.getDescription());
}
```

The scenario shown above is useful for most of the cases.
However, sometimes it's not possible to use the same entity as an object that contains search information like the aforementioned example. In that case, you can annotate another object and use it as a search object. The search object can be any POJO (plain old java object) class, there's no need to be an entity, but if the annotated field has a different name of the entity property, you should provide the annotation with a value for `entityProperty` that should have the correct property name to be used in the entity.

Looking for more examples? See the test source code.
The documentation it's currently under development, so be patient! As soon as possible I'll make it available more examples like this!