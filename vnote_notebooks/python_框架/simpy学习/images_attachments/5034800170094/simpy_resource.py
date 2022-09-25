import simpy
env = simpy.Environment()
res = simpy.Resource(env, capacity=1)

def print_stats(res: simpy.Resource, name):
    print(f'{name}: ')
    # 当前使用资源的用户数/资源最大容量
    print(f'\t{res.count}/{res.capacity}')
    print(f'\tUsers: {res.users}')  #  当前用户
    print(f'\tQueued events: {res.queue}')  # 排队用户

def user01(res: simpy.Resource):
    print_stats(res, 1)
    req = res.request()
    print_stats(res, 2)
    req2 = res.request()
    print_stats(res, 3)
    res.release(req)
    print_stats(res, 4)
    req3 = res.request()
    print_stats(res, 5)
    res.release(req2)
    print_stats(res, 6)
    yield req2
    print_stats(res, 7)
    yield env.timeout(1)

def user02(res: simpy.Resource):
    print_stats(res, 1)
    req = res.request()
    print_stats(res, 2)
    req2 = res.request()
    print_stats(res, 3)
    yield req2
    print_stats(res, 4)
    yield env.timeout(1)

def user03(res: simpy.Resource):
    print_stats(res, 1)
    req = res.request()
    print_stats(res, 2)
    req2 = res.request()
    print_stats(res, 3)
    yield req
    yield req
    print_stats(res, 4)
    res.release(req)
    yield req
    res.release(req)
    print_stats(res, 5)
    yield req2
    print_stats(res, 6)
    yield env.timeout(1)
    
procs = env.process(user01(res))
env.run()