package util_old;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;




/**
  *  Кэш с указанным временем жизни
  */
public class ExpireCache<K, V> {
     private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

     private final Map<K, Holder<V>> cache;
     private final long ttl;


     /**
      * Создать кэш объектов
      *
      * @param t - время жизни по умолчанию, в сек.
      * @param u - время обновления, в сек.
      */
     public ExpireCache(long t, long u) {
         this.cache = new ConcurrentHashMap<K, Holder<V>>();
         this.ttl   = t;

         scheduler.scheduleAtFixedRate(new Runnable() {
             @Override
             public void run() {
                update();
             }
         }, u, u, TimeUnit.SECONDS);

     }


     /**
      * Добавить объект в кэш
      *
      * @param key   - ключ
      * @param value - объект
      * @param ttl   - время жизни, в сек.
      */
     public void put(K key, V value, long ttl) {
         long expires = ttl > 0 ? System.currentTimeMillis() + 1000 * ttl : 0L;

         Holder<V> holder = new Holder<V>(value, expires);

         cache.put(key, holder);
     }


     /**
      * Добавить объект в кэш
      *
      * @param key   - ключ
      * @param value - объект
      */
     public void put(K key, V value) {
         this.put(key, value, this.ttl);
     }


     /**
      * Получить объект из кэша
      *
      * @param key - ключ
      * @return объект из кеша
      */
     public V get(K key) {
         Holder<V> holder = cache.get(key);

         if (holder != null)
             return holder.getValue();

         return null;
     }


     private void update() {
         long time = System.currentTimeMillis();

         for (Map.Entry<K, Holder<V>> e : cache.entrySet())
             if (e.getValue().isExpired(time))
                 cache.remove(e.getKey());

     }


     /**
      *  Остановить пул обновлений
      */
     public static void shutdown() {
         try {
             scheduler.shutdown();

             if (!scheduler.awaitTermination(10, TimeUnit.SECONDS))
                 scheduler.shutdownNow();

         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }
     }



     //*********************************************************************************************
     private static class Holder<V> {
         private final V value;
         private final long expires;


         public Holder(V value, long expires) {
             this.value   = value;
             this.expires = expires;
         }


         public V getValue() {
             return this.value;
         }


         public boolean isExpired(long time) {
             return this.expires > 0 && this.expires < time;
         }


     }


}

