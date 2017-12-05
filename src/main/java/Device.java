import org.bson.Document;

import java.util.Date;

public final class Device extends Document {

  private int id;
  private int value;
  private Date date;

  public Device() {}

  public Device(Device device) {
    this(device.id, device.value);
  }

  public Device(Document document) {

    try {
      // for Doubles
      double dDeviceId = ((double) document.get("deviceId"));
      double dValue = ((double) document.get("value"));

      this.id = ((int) dDeviceId);
      this.value = ((int) dValue);
      this.date = new Date();

    } catch (Exception e) {
      // for Integers
      this.id = (int) document.get("deviceId");
      this.value = (int) document.get("value");
      this.date = new Date();
    }
  }

  public Device(final int deviceId, final int value) {
    this.id = deviceId;
    this.value = value;
    this.date= new Date();
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public int getdeviceId() {
    return id;
  }

  public void setdeviceId(final int deviceId) {
    this.id = deviceId;
  }

  public int getvalue() {
    return value;
  }

  public void setvalue(final int value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Device device = (Device) o;

    if (getdeviceId() != device.getdeviceId() || getvalue() != device.getvalue()) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return "Device{"
            + "id='" + id + "'"
            + ", deviceId='" + id + "'"
            + ", value=" + value
            + "}";
  }
}