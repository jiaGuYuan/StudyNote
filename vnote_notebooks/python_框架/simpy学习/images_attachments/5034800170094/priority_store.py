import simpy

def print_stats(env, store: simpy.Store, name):
    print(f'{name} [{env.now}]: ')
    # 当前使用资源的用户数/资源最大容量
    print(f'\tAvailable items: {store.items}, {len(store.items)}/{store.capacity}')
    print(f'\tPending get requests: {store.get_queue}')
    print(f'\tPending put requests: {store.put_queue}')


def user02(env, pri_store: simpy.PriorityStore):
    # PriorityStore内容使用最小堆排序, 所以值越小优先级越高
    pri_store.put(simpy.PriorityItem(2, 'A'))
    print_stats(env, pri_store, 1)
    pri_store.put(simpy.PriorityItem(0, 'B'))
    print_stats(env, pri_store, 2)
    pri_store.put(simpy.PriorityItem(2, 'C'))
    print_stats(env, pri_store, 3)
    
    pri_store.put(simpy.PriorityItem(0, 'D'))
    print_stats(env, pri_store, 4)
    
    pri_store.put(simpy.PriorityItem(2, 'E'))
    print_stats(env, pri_store, 5)
    
    pri_store.get()
    print_stats(env, pri_store, 6)
    
    pri_store.get()
    print_stats(env, pri_store, 7)
    
    yield pri_store.get()
    print_stats(env, pri_store, 8)
    
    pri_store.get()
    print_stats(env, pri_store, 7)
    yield env.timeout(1)

env = simpy.Environment()

pri_store = simpy.PriorityStore(env, capacity=2)  # 将capacity改为1看看结果
u = env.process(user02(env, pri_store))
env.run(until=5)