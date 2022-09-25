import simpy

def print_stats(env, store: simpy.Store, name):
    print(f'{name} [{env.now}]: ')
    # 当前使用资源的用户数/资源最大容量
    print(f'\tAvailable items: {store.items}, {len(store.items)}/{store.capacity}')
    print(f'\tPending get requests: {store.get_queue}')
    print(f'\tPending put requests: {store.put_queue}')
    # if store.items:
    #     print(f"\t\t {type(store.items)}, {type(store.items[0])}")
    # if store.get_queue:
    #     print(f"\t\t {type(store.get_queue)}, {type(store.get_queue[0])}")

def user01(env, store: simpy.Store):
    store.put('A')
    print_stats(env, store, 1)
    store.put('B')
    print_stats(env, store, 2)
    store.put('C')
    print_stats(env, store, 3)
    yield env.timeout(1)

def user02(env, store: simpy.Store):
    yield store.put('A')
    print_stats(env, store, 1)
    yield store.put('B')
    print_stats(env, store, 2)
    yield store.put('C')
    print_stats(env, store, 3)
    yield env.timeout(1)


def user03(env, store: simpy.Store):
    store.put('A')
    print_stats(env, store, 0)
    req1 = store.get()
    print_stats(env, store, 1)
    req2 = store.get()
    print_stats(env, store, 2)
    req3 = store.get()
    print_stats(env, store, 3)
    yield env.timeout(1)
    req1_data = yield req1
    print(f'req1 data: {req1_data}')
    req2_data = yield req2
    print(f'req2 data: {req2_data}')
    
    

def delay_get(env, store: simpy.Store):
    yield env.timeout(2)
    print_stats(env, store, 'x01')
    req_get = store.get()
    print(f'get req: {req_get}')
    print_stats(env, store, 'x02')
    data = yield req_get
    print(f'get data: {data}')
    print_stats(env, store, 'x03')
    
    
env = simpy.Environment()
store = simpy.Store(env, capacity=2)
u = env.process(user03(env, store))
# d = env.process(delay_get(env, store))
env.run(until=5)



