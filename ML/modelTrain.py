import tensorflow as tf

train_dir = './data_source/face_expression/images/images/train'
test_dir = './data_source/face_expression/images/images/validation'

dataGenerator = tf.keras.preprocessing.image.ImageDataGenerator(rescale=1. / 255, horizontal_flip=True,
                                                                validation_split=0.2)

training_data = dataGenerator.flow_from_directory(train_dir, batch_size=64, target_size=(48, 48), shuffle=True,
                                                  color_mode='grayscale', class_mode='categorical', subset='training')
validation_set = dataGenerator.flow_from_directory(train_dir, batch_size=64, target_size=(48, 48), shuffle=True,
                                                   color_mode='grayscale', class_mode='categorical',
                                                   subset='validation')

testDataGenerator = tf.keras.preprocessing.image.ImageDataGenerator(rescale=1. / 255, horizontal_flip=True)

test_data = testDataGenerator.flow_from_directory(test_dir, batch_size=64, target_size=(48, 48), shuffle=True,
                                                  color_mode='grayscale', class_mode='categorical')


def create_model():
    weight_decay = 1e-4
    model = tf.keras.models.Sequential()

    model.add(
        tf.keras.layers.Conv2D(64, (4, 4), padding='same', kernel_regularizer=tf.keras.regularizers.l2(weight_decay),
                               input_shape=(48, 48, 1)))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(
        tf.keras.layers.Conv2D(128, (4, 4), padding='same', kernel_regularizer=tf.keras.regularizers.l2(weight_decay)))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(tf.keras.layers.MaxPool2D(pool_size=(2, 2)))
    model.add(tf.keras.layers.Dropout(0.2))

    model.add(
        tf.keras.layers.Conv2D(128, (4, 4), padding='same', kernel_regularizer=tf.keras.regularizers.l2(weight_decay)))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(tf.keras.layers.MaxPool2D(pool_size=(2, 2)))
    model.add(tf.keras.layers.Dropout(0.3))

    model.add(
        tf.keras.layers.Conv2D(256, (4, 4), padding='same', kernel_regularizer=tf.keras.regularizers.l2(weight_decay)))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(
        tf.keras.layers.Conv2D(64, (4, 4), padding='same', kernel_regularizer=tf.keras.regularizers.l2(weight_decay)))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.BatchNormalization())
    model.add(tf.keras.layers.MaxPool2D(pool_size=(2, 2)))
    model.add(tf.keras.layers.Dropout(0.4))
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(64, activation="linear"))
    model.add(tf.keras.layers.Activation('relu'))
    model.add(tf.keras.layers.Dense(7, activation='softmax'))

    return model


model = create_model()

model.compile(loss='categorical_crossentropy', optimizer=tf.keras.optimizers.Adam(), metrics=['accuracy'])

model.summary()

checkpointer = [
    tf.keras.callbacks.EarlyStopping(monitor='val_accuracy', verbose=1, restore_best_weights=True, mode="max",
                                     patience=10),
    tf.keras.callbacks.ModelCheckpoint(
        filepath='weight/model-{epoch}.hdf5',
        monitor="val_accuracy",
        verbose=1,
        mode="max")]

steps_per_epoch = training_data.n // training_data.batch_size
validation_steps = validation_set.n // validation_set.batch_size

history = model.fit(x=training_data,
                    validation_data=validation_set,
                    epochs=100,
                    callbacks=[checkpointer],
                    steps_per_epoch=steps_per_epoch,
                    validation_steps=validation_steps)

model.save("./model/emotion_detection_model_5.h5")
