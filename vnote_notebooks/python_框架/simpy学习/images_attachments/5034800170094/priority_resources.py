import simpy


def print_stats(res: simpy.Resource, name):
    print(f'{name}: ')
    # 当前使用资源的用户数/资源最大容量
    print(f'\t{res.count}/{res.capacity}')
    print(f'\tUsers: {res.users}')  # 当前用户
    print(f'\tQueued events: {res.queue}')  # 排队用户


def resource_user(name, env, resource, wait, prio):
    yield env.timeout(wait)
    with resource.request(priority=prio) as req:
        print_stats(resource, name)
        # print(f'\t{name} requesting at {env.now} with priority={prio}')
        yield req
        # print(f'{name} got resource at {env.now}')
        yield env.timeout(3)


env = simpy.Environment()
res = simpy.PriorityResource(env, capacity=2)
p1 = env.process(resource_user('A', env, res, wait=0, prio=0))
p2 = env.process(resource_user('B', env, res, wait=0, prio=0))
p3 = env.process(resource_user('C', env, res, wait=0, prio=-1))
p4 = env.process(resource_user('D', env, res, wait=0, prio=-2))
p5 = env.process(resource_user('E', env, res, wait=0, prio=-3))
env.run()
