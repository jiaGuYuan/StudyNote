import numpy as np
# data.shape: (3,2,3)
data = np.array([[[1,2,3],
                  [4,5,6]],
                 [[7,8,8],
                  [10,11,3]],
                 [[1,5,7],
                  [5,3,1]]])
print(data)
print("**********")

# output0.shape: (2,3)
output0 = np.sum(data, axis=0)  # 消去了第0个维度
print(output0)
print("===")
tmp = np.add(np.add(data[0], data[1]), data[2])
print(tmp)
print("**********")

# output1.shape: (3,3)
output1 = np.sum(data, axis=1)  # 消去了第1个维度
print(output1)
print("===")
tmp1 = np.add(data[0][0], data[0][1])
tmp2 = np.add(data[1][0], data[1][1])
tmp3 = np.add(data[2][0], data[2][1])
print(np.array([tmp1, tmp2, tmp3]))
print("**********")

# output2.shape: (3,2)
output2 = np.sum(data, axis=2)  # 消去了第2个维度
print(output2)
print("===")
tmp00 = np.add(np.add(data[0][0][0], data[0][0][1]), data[0][0][2])
tmp01 = np.add(np.add(data[0][1][0], data[0][1][1]), data[0][1][2])
tmp10 = np.add(np.add(data[1][0][0], data[1][0][1]), data[1][0][2])
tmp11 = np.add(np.add(data[1][1][0], data[1][1][1]), data[1][1][2])
tmp20 = np.add(np.add(data[2][0][0], data[2][0][1]), data[2][0][2])
tmp21 = np.add(np.add(data[2][1][0], data[2][1][1]), data[2][1][2])
print(np.array([[tmp00, tmp01],
                [tmp10, tmp11],
                [tmp20, tmp21]]
                ))
print("**********")