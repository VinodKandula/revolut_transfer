package revoluttransfer.repositories

interface CommonRepository<I, T> {

    fun getList(): List<T>

    fun getById(id: I): T

    fun create(entity: T)

    fun update(id: I)

    fun removeBy(id: I)

    fun remove(entity: T)
}