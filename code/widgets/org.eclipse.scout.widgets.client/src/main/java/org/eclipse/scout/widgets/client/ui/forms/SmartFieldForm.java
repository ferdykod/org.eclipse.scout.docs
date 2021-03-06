/*******************************************************************************
 * Copyright (c) 2015 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.widgets.client.ui.forms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.AbstractStatusMenuMapping;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractProposalField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.ContentAssistFieldTable;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.IContentAssistField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.IProposalChooser;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.IProposalChooserProvider;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.ISmartField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.TableProposalChooser;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.SleepUtil;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.widgets.client.services.lookup.LocaleLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.RemoteLocaleLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.UserContentListLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.UserContentTreeLookupCall;
import org.eclipse.scout.widgets.client.ui.desktop.outlines.IAdvancedExampleForm;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.CloseButton;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.BrowseAutoExpandAllField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.BrowseHierarchyField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.BrowseMaxRowCountField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.GetValueField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.ListEntriesField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.ListSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.TreeEntriesField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ConfigurationBox.TreeSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.ProposalFieldWithListContentBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.ProposalFieldWithListContentBox.DefaultProposalField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.ProposalFieldWithListContentBox.MandatoryProposalField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithListGroupBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithListGroupBox.DefaultField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithListGroupBox.DisabledField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithListGroupBox.MandatoryField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithListGroupBox.SmartFieldWithCustomTableField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.DefaultSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.DisabledSmartFieldField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.MandatorySmartfieldField;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.SampleContentButton;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.SeleniumTestMenu.SlowProposalChooserMenu;
import org.eclipse.scout.widgets.client.ui.forms.SmartFieldForm.MainBox.SeleniumTestMenu.SwitchLookupCallMenu;
import org.eclipse.scout.widgets.client.ui.template.formfield.AbstractUserTreeField;
import org.eclipse.scout.widgets.shared.services.code.ColorsCodeType;
import org.eclipse.scout.widgets.shared.services.code.EventTypeCodeType;
import org.eclipse.scout.widgets.shared.services.code.IndustryICBCodeType;
import org.eclipse.scout.widgets.shared.services.code.IndustryICBCodeType.ICB9000.ICB9500.ICB9530.ICB9537;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test form for the old smart field (since 7.0). Do not migrate to SmartField2. Will be removed with 7.1
 */
@Order(3000.0)
public class SmartFieldForm extends AbstractForm implements IAdvancedExampleForm {

  private static final Logger LOG = LoggerFactory.getLogger(SmartFieldForm.class);

  private boolean m_delayEnabled = false;
  private boolean m_localLookupCall = true;

  public SmartFieldForm() {
    super();
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("SmartField");
  }

  @Override
  public void startPageForm() {
    startInternal(new PageFormHandler());
  }

  /**
   * @return the BrowseAutoExpandAllField
   */
  public BrowseAutoExpandAllField getBrowseAutoExpandAllField() {
    return getFieldByClass(BrowseAutoExpandAllField.class);
  }

  /**
   * @return the BrowseHierarchyField
   */
  public BrowseHierarchyField getBrowseHierarchyField() {
    return getFieldByClass(BrowseHierarchyField.class);
  }

  /**
   * @return the BrowseMaxRowCountField
   */
  public BrowseMaxRowCountField getBrowseMaxRowCountField() {
    return getFieldByClass(BrowseMaxRowCountField.class);
  }

  @Override
  public CloseButton getCloseButton() {
    return getFieldByClass(CloseButton.class);
  }

  /**
   * @return the DisabledSmartFieldField
   */
  public DisabledSmartFieldField getDisabledSmartFieldField() {
    return getFieldByClass(DisabledSmartFieldField.class);
  }

  public ExamplesBox getExamplesBox() {
    return getFieldByClass(ExamplesBox.class);
  }

  /**
   * @return the DefaultField
   */
  public DefaultField getDefaultField() {
    return getFieldByClass(DefaultField.class);
  }

  public DisabledField getDisabledField() {
    return getFieldByClass(DisabledField.class);
  }

  /**
   * @return the MandatoryField
   */
  public MandatoryField getMandatoryField() {
    return getFieldByClass(MandatoryField.class);
  }

  /**
   * @return the MandatorySmartfieldField
   */
  public MandatorySmartfieldField getMandatorySmartfieldField() {
    return getFieldByClass(MandatorySmartfieldField.class);
  }

  public ConfigurationBox getConfigurationBox() {
    return getFieldByClass(ConfigurationBox.class);
  }

  /**
   * @return the DefaultSmartField
   */
  public DefaultSmartField getDefaultSmartField() {
    return getFieldByClass(DefaultSmartField.class);
  }

  public DefaultProposalField getDefaultProposalField() {
    return getFieldByClass(DefaultProposalField.class);
  }

  public MandatoryProposalField getMandatoryProposalField() {
    return getFieldByClass(MandatoryProposalField.class);
  }

  /**
   * @return the GetCheckedKeysField
   */
  public GetValueField getGetValueField() {
    return getFieldByClass(GetValueField.class);
  }

  /**
   * @return the ListSmartField
   */
  public ListSmartField getListSmartField() {
    return getFieldByClass(ListSmartField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  /**
   * @return the TreeEntriesField
   */
  public ListEntriesField getListEntriesField() {
    return getFieldByClass(ListEntriesField.class);
  }

  /**
   * @return the TreeEntriesField
   */
  public TreeEntriesField getTreeEntriesField() {
    return getFieldByClass(TreeEntriesField.class);
  }

  public SmartFieldWithCustomTableField getSmartFieldWithCustomTableField() {
    return getFieldByClass(SmartFieldWithCustomTableField.class);
  }

  public SmartFieldWithListGroupBox getSmartFieldWithListGroupBox() {
    return getFieldByClass(SmartFieldWithListGroupBox.class);
  }

  public SmartFieldWithTreeGroupBox getSmartFieldWithTreeGroupBox() {
    return getFieldByClass(SmartFieldWithTreeGroupBox.class);
  }

  public ProposalFieldWithListContentBox getProposalFieldWithListContentBox() {
    return getFieldByClass(ProposalFieldWithListContentBox.class);
  }

  /**
   * @return the SampleContentButton
   */
  public SampleContentButton getSampleContentButton() {
    return getFieldByClass(SampleContentButton.class);
  }

  /**
   * @return the TreeSmartField
   */
  public TreeSmartField getTreeSmartField() {
    return getFieldByClass(TreeSmartField.class);
  }

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class ExamplesBox extends AbstractGroupBox {

      @Override
      protected int getConfiguredGridColumnCount() {
        return 3;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Examples");
      }

      @Order(1000)
      @ClassId("2ef66f5c-cb5a-4c82-885a-5bdad18eedb4")
      public class SmartFieldWithListGroupBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("SmartFieldWithListContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(30)
        public class DefaultField extends AbstractSmartField<Locale> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }

          @Override
          protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
            return (Class<? extends ILookupCall<Locale>>) /*Remote*/LocaleLookupCall.class;
          }

          @Order(1000)
          @ClassId("efdb5f17-d14e-4700-be1a-2cb0689d36c2")
          public class NewMenu extends AbstractMenu {
            @Override
            protected String getConfiguredText() {
              return "Add new language";
            }

            @Override
            protected Set<? extends IMenuType> getConfiguredMenuTypes() {
              return CollectionUtility.hashSet(ValueFieldMenuType.Null);
            }

            @Override
            protected void execAction() {
              MessageBoxes.createOk()
                  .withHeader(TEXTS.get("ThanksForClickingMe"))
                  .withBody(TEXTS.get("NewLanguageMessage"))
                  .show();
            }
          }

          public class NewMenuStatusMapping extends AbstractStatusMenuMapping {

            @Override
            protected Class<? extends IMenu> getConfiguredMenu() {
              return NewMenu.class;
            }

            @Override
            protected List<Integer> getConfiguredCodes() {
              return Arrays.asList(ISmartField.NO_RESULTS_ERROR_CODE);
            }

          }
        }

        @Order(40)
        public class MandatoryField extends AbstractSmartField<Color> {

          @Override
          protected Class<? extends ICodeType<?, Color>> getConfiguredCodeType() {
            return ColorsCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }

          @Override
          protected void execChangedValue() {
            Color color = getValue();
            if (color == null) {
              setBackgroundColor(null);
            }
            else {
              String hex = Integer.toHexString(color.getRGB()).substring(2);
              setBackgroundColor(hex);
            }
          }
        }

        @Order(50)
        public class DisabledField extends AbstractSmartField<Color> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Color>> getConfiguredCodeType() {
            return ColorsCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValue(new Color(255, 255, 255));
          }
        }

        @Order(55)
        @ClassId("e5fdd969-8372-45d6-8225-bd3358536d17")
        public class SmartFieldWithCustomTableField extends AbstractSmartField<Locale> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("CustomTable");
          }

          @Override
          protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
            return (Class<? extends ILookupCall<Locale>>) /*Remote*/LocaleLookupCall.class;
          }

          @Order(10.0)
          public class Table extends ContentAssistFieldTable<Locale> {

            public CountryColumn getCountryColumn() {
              return getColumnSet().getColumnByClass(CountryColumn.class);
            }

            public LanguageColumn getLanguageColumn() {
              return getColumnSet().getColumnByClass(LanguageColumn.class);
            }

            @Override
            protected boolean getConfiguredHeaderVisible() {
              return true;
            }

            @Order(100.0)
            public class CountryColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Country");
              }

              @Override
              protected int getConfiguredWidth() {
                return 60;
              }
            }

            @Order(110.0)
            public class LanguageColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Language");
              }

              @Override
              protected int getConfiguredWidth() {
                return 60;
              }
            }
          }
        }

      }

      @Order(2000)
      @ClassId("d2224f6f-a6cd-4c06-99ca-960bbd3d0153")
      public class SmartFieldWithTreeGroupBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("SmartFieldWithTreeContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(70)
        public class DefaultSmartField extends AbstractSmartField<Long> {

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return IndustryICBCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }
        }

        @Order(80)
        public class MandatorySmartfieldField extends AbstractSmartField<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return IndustryICBCodeType.class;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(90)
        public class DisabledSmartFieldField extends AbstractSmartField<Long> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return IndustryICBCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValue(ICB9537.ID);
          }
        }
      }

      @Order(3000)
      @ClassId("da633124-cdf8-4eb5-8905-f3b4c442e5a9")
      public class ProposalFieldWithListContentBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ProposalFieldWithListContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(110)
        public class DefaultProposalField extends AbstractProposalField<Long> {

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }

          @Override
          protected IProposalChooserProvider<Long> createProposalChooserProvider() {
            return new P_ProposalChooserProvider();
          }

        }

        @Order(120)
        public class MandatoryProposalField extends AbstractProposalField<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(130)
        public class DisabledProposalField extends AbstractProposalField<Long> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValue(TEXTS.get("Public"));
          }
        }
      }

    }

    @Order(20)
    public class ConfigurationBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Configure");
      }

      @Override
      protected int getConfiguredGridColumnCount() {
        return 3;
      }

      @Order(10)
      public class ListSmartField extends AbstractSmartField<String> {

        private boolean m_throttled = false;

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ListSmartField");
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return (Class<? extends ILookupCall<String>>) UserContentListLookupCall.class;
        }

        @Override
        protected void execPrepareLookup(ILookupCall<String> call) {
          if (m_throttled) {
            SleepUtil.sleepSafe(1, TimeUnit.SECONDS);
          }
          super.execPrepareLookup(call);
        }

        @Override
        protected void execChangedValue() {
          if (m_throttled) {
            SleepUtil.sleepSafe(1, TimeUnit.SECONDS);
          }
          super.execChangedValue();
        }

        public boolean toggleThrottle() {
          m_throttled = !m_throttled;
          return m_throttled;
        }

      }

      @Order(20)
      public class GetValueField extends AbstractStringField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("GetValue");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return SmartFieldForm.MainBox.ConfigurationBox.ListSmartField.class;
        }

        @Override
        protected void execChangedMasterValue(Object newMasterValue) {
          if (newMasterValue != null) {
            setValue(newMasterValue.toString());
          }
          else {
            setValue(null);
          }
        }
      }

      @Order(30)
      public class ListEntriesField extends AbstractUserTreeField {

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ListContent");
        }

        @Override
        protected void execChangedValue() {
          List<Node> nodes = parseFieldValue(false);
          ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();
          addNodesToLookupRows(nodes, rows);

          ((UserContentListLookupCall) getListSmartField().getLookupCall()).setLookupRows(rows);
          getListSmartField().initField();
        }
      }

      @Order(40)
      public class BrowseMaxRowCountField extends AbstractIntegerField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseMaxRowCount");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected Integer getConfiguredMinValue() {
          return 0;
        }

        @Override
        protected void execChangedValue() {
          getListSmartField().setBrowseMaxRowCount(NumberUtility.nvl(getValue(), 100));
          getTreeSmartField().setBrowseMaxRowCount(NumberUtility.nvl(getValue(), 100));
        }
      }

      @Order(50)
      public class EnableActiveFilterField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("EnableActiveFilter");
        }

        @Override
        protected void execChangedValue() {
          getListSmartField().setActiveFilterEnabled(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getListSmartField().isActiveFilterEnabled());
        }
      }

      @Order(70)
      public class TreeSmartField extends AbstractSmartField<String> {

        @Override
        protected boolean getConfiguredBrowseAutoExpandAll() {
          return false;
        }

        @Override
        protected boolean getConfiguredBrowseHierarchy() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("TreeSmartField");
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return (Class<? extends ILookupCall<String>>) UserContentTreeLookupCall.class;
        }
      }

      @Order(80)
      public class GetValueTreeSmartFieldField extends AbstractStringField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("GetValue");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return SmartFieldForm.MainBox.ConfigurationBox.TreeSmartField.class;
        }

        @Override
        protected void execChangedMasterValue(Object newMasterValue) {
          if (newMasterValue != null) {
            setValue(newMasterValue.toString());
          }
          else {
            setValue(null);
          }
        }
      }

      @Order(90)
      public class TreeEntriesField extends AbstractUserTreeField {

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("TreeContent");
        }

        @Override
        protected void execChangedValue() {
          List<Node> nodes = parseFieldValue(true);
          List<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();

          addNodesToLookupRows(nodes, rows);
          ((UserContentTreeLookupCall) getTreeSmartField().getLookupCall()).setLookupRows(rows);
        }
      }

      @Order(100)
      public class BrowseHierarchyField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseHierarchy");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected void execChangedValue() {
          getTreeSmartField().setBrowseHierarchy(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getTreeSmartField().isBrowseHierarchy());
        }
      }

      @Order(110)
      public class BrowseAutoExpandAllField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseAutoExpandAll");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected void execChangedValue() {
          getTreeSmartField().setBrowseAutoExpandAll(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getTreeSmartField().isBrowseAutoExpandAll());
        }
      }

      @Order(120)
      public class PlaceholderField extends AbstractPlaceholderField {

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }

      }
    }

    @Order(30)
    public class SampleContentButton extends AbstractButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SampleContent");
      }

      @Override
      protected void execClickAction() {
        ListEntriesField listEntries = getListEntriesField();
        listEntries.setValue(TEXTS.get("ListBoxUserContent"));

        TreeEntriesField treeEntries = getTreeEntriesField();
        treeEntries.setValue(TEXTS.get("TreeUserContent"));
      }
    }

    @Order(35)
    public class SeleniumTestMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Selenium");
      }

      @Order(10)
      public class WildcardMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("Wildcard");
        }

        @Order(10)
        public class AutoPrefixWildcardMenu extends AbstractMenu {

          private boolean m_active;

          @Override
          protected void execInitAction() {
            m_active = ClientSessionProvider.currentSession().getDesktop().isAutoPrefixWildcardForTextSearch();
            updateText();
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("EnableAutoPrefixWildcard");
          }

          @Override
          protected void execAction() {
            m_active = !m_active;
            ClientSessionProvider.currentSession().getDesktop().setAutoPrefixWildcardForTextSearch(m_active);
            updateText();
          }

          private void updateText() {
            setText(TEXTS.get(m_active ? "DisableAutoPrefixWildcard" : "EnableAutoPrefixWildcard"));
          }
        }

        @Order(20)
        public class ChangeWildcard1Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("*");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "*");
          }
        }

        @Order(30)
        public class ChangeWildcard2Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard(".*");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", ".*");
          }
        }

        @Order(40)
        public class ChangeWildcard3Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("\\");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "\\");
          }
        }

        @Order(50)
        public class ChangeWildcard4Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("°");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "°");
          }
        }
      }

      @Order(20)
      public class ShowSmartFieldValueMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ShowValue", "Smart field");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ShowValueOfDefaultField", "Smart field");
        }

        @Override
        protected void execAction() {
          MessageBoxes.createOk().withBody(getDefaultField().getValue() + "").show();
        }

        @Override
        protected String getConfiguredKeyStroke() {
          return "Ctrl-Alt-P";
        }
      }

      @Order(30)
      public class ShowProposalFieldValueMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ShowValue", "Proposal field");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ShowValueOfDefaultField", "Proposal field");
        }

        @Override
        protected void execAction() {
          MessageBoxes.createOk().withBody(getDefaultProposalField().getValue() + "").show();
        }

        @Override
        protected String getConfiguredKeyStroke() {
          return "Ctrl-Alt-Q";
        }
      }

      @Order(40)
      public class SlowProposalChooserMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("EnableSlowProposalChooser");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("SlowProposalChooserDisabledTooltip");
        }

        @Override
        protected void execAction() {
          setDelayEnabled(!m_delayEnabled);
        }
      }

      @Order(50)
      public class SwitchLookupCallMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("SwitchToRemote");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("SwitchToRemoteTooltip");
        }

        @Override
        protected void execAction() {
          setLocalLookupCall(!m_localLookupCall);
        }
      }

      @Order(60)
      public class SeleniumResetMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("Reset");
        }

        @Override
        protected void execAction() {
          setDelayEnabled(false);
          setLocalLookupCall(true);
        }
      }

      @Order(40)
      public class ThrottleSmartFieldMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ThrottleSmartFieldRequests");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ThrottleSmartFieldRequestsTooltip");
        }

        @Override
        protected void execAction() {
          boolean throttled = getListSmartField().toggleThrottle();
          if (throttled) {
            setText(TEXTS.get("NoThrottling"));
            setTooltipText(TEXTS.get("NoThrottlingSmartFieldRequestsTooltip"));
          }
          else {
            setText(TEXTS.get("ThrottleSmartFieldRequests"));
            setTooltipText(TEXTS.get("ThrottleSmartFieldRequestsTooltip"));
          }
        }

      }

    }

    @Order(40)
    public class CloseButton extends AbstractCloseButton {
    }
  }

  public class PageFormHandler extends AbstractFormHandler {
  }

  public void setDelayEnabled(boolean delayEnabled) {
    m_delayEnabled = delayEnabled;
    IMenu menu = getMainBox().getMenuByClass(SlowProposalChooserMenu.class);
    menu.setText(TEXTS.get(m_delayEnabled ? "DisableSlowProposalChooser" : "EnableSlowProposalChooser"));
    menu.setTooltipText(TEXTS.get(m_delayEnabled ? "SlowProposalChooserEnabledTooltip" : "SlowProposalChooserDisabledTooltip"));
    LOG.debug("Slow proposal chooser is {}", (m_delayEnabled ? "enabled" : "disabled"));
  }

  public void setLocalLookupCall(boolean localLookupCall) {
    m_localLookupCall = localLookupCall;
    IMenu menu = getMainBox().getMenuByClass(SwitchLookupCallMenu.class);
    getDefaultField().setLookupCall(m_localLookupCall ? new LocaleLookupCall() : new RemoteLocaleLookupCall());
    menu.setText(TEXTS.get(m_localLookupCall ? "SwitchToRemote" : "SwitchToLocal"));
    menu.setTooltipText(TEXTS.get(m_localLookupCall ? "SwitchToRemoteTooltip" : "SwitchToLocalTooltip"));
    LOG.debug("Switched lookup-call of DefaultField to {} instance", (m_localLookupCall ? "local" : "remote"));
  }

  private void changeWildcard(String wildcard) {
    getDefaultField().setWildcard(wildcard);
    getMandatoryField().setWildcard(wildcard);
    getDefaultSmartField().setWildcard(wildcard);
    getMandatorySmartfieldField().setWildcard(wildcard);
    getDefaultProposalField().setWildcard(wildcard);
    getMandatoryProposalField().setWildcard(wildcard);
    getListSmartField().setWildcard(wildcard);
    getTreeSmartField().setWildcard(wildcard);
  }

  /**
   * Custom proposal chooser provider is only used to simulate a slow connection when user clicks on a row in the
   * proposal chooser
   */
  private class P_ProposalChooserProvider implements IProposalChooserProvider<Long> {

    @Override
    public IProposalChooser<?, Long> createProposalChooser(IContentAssistField<?, Long> proposalField, boolean allowCustomText) {
      return new TableProposalChooser<Long>(proposalField, allowCustomText) {
        @Override
        protected void execResultTableRowClicked(ITableRow row) {
          if (m_delayEnabled) {
            SleepUtil.sleepSafe(600, TimeUnit.MILLISECONDS);
          }
          super.execResultTableRowClicked(row);
        }
      };
    }

  }

}
