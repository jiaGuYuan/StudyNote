import os
import shutil

def remove_pycache(root_dir):
    for root, dirs, _ in os.walk(root_dir):
        if '__pycache__' in dirs:
            pycache_path = os.path.join(root, '__pycache__')
            try:
                shutil.rmtree(pycache_path)
                print(f"Deleted: {pycache_path}")
            except Exception as e:
                print(f"Failed to delete {pycache_path}: {e}")

if __name__ == '__main__':
    remove_pycache(r'D:\Software\Anaconda3\envs\skd')  # 指定目录
        