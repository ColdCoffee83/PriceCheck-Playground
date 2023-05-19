describe('test equityCheckCreateFunctions', function() {
    it('should correctly add weekdays', function() {
        let currentDate = new Date(2017, 3, 4);
        let days = 2;
        let result = equityCheckCreateFunctions.addWeekdays(currentDate, days);
        assert.strictEqual(result.getDate(), 6);
    })

    it('should not add any days to the date', function() {
        let currentDate = new Date(2017, 3, 4);
        let days = 0;
        let result = equityCheckCreateFunctions.addWeekdays(currentDate, days);
        assert.strictEqual(result.getDate(), 4);
    })    
})

describe("selectOfficePOC function", () => {
    it("should call $.post function with correct parameters", () => {
      const mockData = {
        displayName: "John Doe",
        organization: "ACME Inc.",
        securePhone: "123-456-7890"
      };
  
      const mockResult = {
        success: true,
        obj: {
          id: 1
        }
      };
  
      spyOn($, "post").and.callFake((url, postData, successCallback) => {
        expect(url).toEqual(appContext + "/ajax/americano/equityCheck/setOfficePOC/" + checkEquildValue);
        expect(postData).toEqual(mockData);
        successCallback(mockResult);
      });
  
      spyOn(dialogUtil, "closeDialog");
  
      selectOfficePOC(mockData);
  
      expect($("#officePOCName").text()).toEqual(mockData.displayName);
      expect($("#officePOCOrg").text()).toEqual(mockData.organization);
      expect($("#officePOCSecurePhone span.orange").text()).toEqual(mockData.securePhone);
      expect($("#officePOCId").val()).toEqual(mockResult.obj.id.toString());
      expect(dialogUtil.closeDialog).toHaveBeenCalled();
    });
  
    it("should call dialogUtil.alert function when result.success is false", () => {
      const mockData = {
        displayName: "John Doe",
        organization: "ACME Inc.",
        securePhone: "123-456-7890"
      };
  
      const mockResult = {
        success: false,
        message: "Error"
      };
  
      spyOn($, "post").and.callFake((url, postData, successCallback) => {
        successCallback(mockResult);
      });
  
      spyOn(dialogUtil, "alert");
  
      selectOfficePOC(mockData);
  
      expect(dialogUtil.alert).toHaveBeenCalledWith("Error adding Officer", mockResult.message);
    });
  });
  
