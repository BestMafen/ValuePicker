## ValuePicker 一个类似尺子的选值控件,支持水平、垂直和半圆三种模式
![水平](https://github.com/BestMafen/Pic/blob/master/ValuePicker/S60908-223031.jpg?raw=true)
![垂直](https://github.com/BestMafen/Pic/blob/master/ValuePicker/S60908-223046.jpg?raw=true)
![半圆](https://github.com/BestMafen/Pic/blob/master/ValuePicker/S60908-223402.jpg?raw=true)
### 1. 水平模式  
- xml布局：

```
<ValuePicker.ValuePickerH
            android:id="@+id/h1"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"/>
```

- java代码：

```
ValuePickerH h1 = (ValuePickerH) findViewById(R.id.h1);
        h1.setParams(ValuePickerH.Gravity.TOP, ValuePickerH.Orientation.UP, 20, 100, 0.5f, 50);
h1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
```

---

### 2. 垂直模式
- xml布局  

```
<ValuePicker.ValuePickerV
                android:id="@+id/v1"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="1"/>
```

- java代码：

```
ValuePickerV v1 = (ValuePickerV) findViewById(R.id.v1);
        v1.setParams(ValuePickerV.Gravity.START, ValuePickerV.Orientation.LEFT, 20, 100, 0.5f, 50);
 v1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
```

---

### 3. 半圆模式，当Graviry为START和END时，半径为高度的1/2；当Graviry为TOP和BOTTOM时，半径为宽度的1/2。
- xml布局

```
<ValuePicker.ValuePickerC
        android:id="@+id/c1"
        android:layout_width="120dp"
        android:layout_height="200dp"
        android:layout_centerVertical="true"/>
```

- java代码：

```
ValuePickerC c1 = (ValuePickerC) findViewById(R.id.c1);
        c1.setParams(ValuePickerC.Gravity.START, 20, 100, 0.5f, 50);
c1.setOnValuePickedListener(new OnValuePickedListener() {
            @Override
            public void onValueSelected(float value) {
                tv1.setText(String.valueOf(value));
            }
        });
```

---
#### ==联系作者：740178870==