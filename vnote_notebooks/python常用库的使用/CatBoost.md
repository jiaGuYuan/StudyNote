# CatBoost
## 自定义损失 与 验证集上的评估指标
```
from catboost import CatBoostRegressor

# 自定义损失 MSE
class CustomMseObjective(object):
    # 参数:approxes: 预测值--containers类型(如List)
    #     targets: 真实值--containers类型(如List)
    #     weights: 权重,为None表示平均权重--containers类型(如List)
    # 返回:(损失函数关于预测值的一阶导数,损失函数关于预测值的二阶导数)
    def calc_ders_range(self, approxes, targets, weights):
        # 不使用weights
        assert len(approxes) == len(targets)
        if weights is not None:
            assert len(weights) == len(approxes)

        targets = np.array(targets)
        approxes = np.array(approxes)
        m = approxes.shape[0]
        der1 = 2/m * (targets-approxes)
        der2 = -2/m * np.ones((m,))
        return tuple(zip(der1, der2))
    
# 自定义评估指标 MSE
class CustomMseMetric(object):
    def get_final_error(self, error, weight):
        return error / (weight + 1e-38)

    # 该指标是否取值越大越好
    def is_max_optimal(self):
        return False

    def evaluate(self, approxes, target, weight):
        assert len(approxes) == 1
        assert len(target) == len(approxes[0])
        approx = approxes[0]
        approx = np.array(approx)
        target = np.array(target)

        score = mean_squared_error(target, approx)
        # 不使用weight,sum(weight)返回1即可(相当于直接归一化了)
        return score, 1

idx = np.random.choice(range(X_data_np.shape[0]), size=5000, replace=False)
trainset = X_data_np[idx[0:3000]]
train_labels = Y_data_np[idx[0:3000]]
val_set = X_data_np[idx[3000:]]
val_labels = Y_data_np[idx[3000:]]

model = CatBoostRegressor(iterations=10, learning_rate=1, depth=1,
                          loss_function=RmseObjective(),  #使用自定义损失
                          eval_metric=RmseMetric(), #使用自定义评估指标
                          random_seed=1)
# 设置loss_function='RMSE',eval_metric='RMSE'来验证自定义RMSE效果
# model = CatBoostRegressor(iterations=10, learning_rate=1, depth=1,
#                           loss_function='RMSE',
#                           eval_metric='RMSE',
#                           random_seed=2020)

fit_model = model.fit(trainset, train_labels, verbose=True, eval_set=(val_set, val_labels))
print('模型R2评分:', fit_model.score(val_set, val_labels)) # mode.score得到的是R2评分
#print('模型在训练过程中验证集上的预测结果: ', fit_model.get_test_eval())
print('模型训练过程最优评分(使用自定义的RmseMetric)\r\n\t', fit_model.get_best_score())
#print('每一次提升的评分(RmseMetric)\r\n\t', fit_model.get_evals_result())


# 使用自定义损失函数只允许 prediction_type='RawFormulVal'
pred = fit_model.predict(val_set, prediction_type='RawFormulaVal')
# 手动计算RMSE
rmse = np.sqrt(np.mean((pred - val_labels) ** 2))
#print('Test集\r\n\tPredictions: ', pred, '\n\tget_test_eval: ', fit_model.get_test_eval())
print('手动在Test集上计算的RMSE: ', rmse)

pred = fit_model.predict(trainset, prediction_type='RawFormulaVal')
rmse = np.sqrt(np.mean((pred - train_labels) ** 2))
print('手动在Train集上计算的RMSE: ', rmse)
```


