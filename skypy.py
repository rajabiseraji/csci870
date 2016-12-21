#!local/bin/python

import itertools
import numpy as np
import matplotlib.pyplot as plt

from sklearn.metrics import confusion_matrix

def plot_confusion_matrix(y, p, classes=None, normalize=False, title='Confusion Matrix', cmap=plt.cm.Blues, fname=None):
    cm = confusion_matrix(y, p)

    fig = plt.figure()
    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()

    if classes:
        tick_marks = np.arange(len(classes))
        plt.xticks(tick_marks, classes, rotation=45)
        plt.yticks(tick_marks, classes)

    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]

    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        s = '%.2f' % cm[i, j]
        plt.text(j, i, s, horizontalalignment="center", color="white" if cm[i, j] > thresh else "black")

    plt.xlabel('Predicted Location')
    plt.ylabel('True Location')
    plt.tight_layout()

    if fname:
        fig.savefig(fname)
        plt.close()
    else:
        plt.show()
